package com.planner.api.service;

import com.planner.api.entity.*;
import com.planner.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TemplatePopulationIntegrationTest {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ScheduleRuleRepository scheduleRuleRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntryRepository entryRepository;

    private User testUser;
    private Calendar testCalendar;
    private Template testTemplate;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User("Integration Test User");
        testUser = userRepository.save(testUser);

        // Create test calendar
        testCalendar = new Calendar(testUser, "Test Calendar");
        testCalendar = calendarRepository.save(testCalendar);

        // Create test template
        testTemplate = new Template();
        testTemplate.setName("Morning Routine");
        testTemplate.setNote("Daily morning routine");
        testTemplate.setOwner(testUser);
        testTemplate = templateService.createTemplate(testTemplate, testUser, null);

        // Add entries to template
        Entry entry1 = new Entry(testUser, EntryType.TEXT, EntrySubjectType.TEMPLATE,
                testTemplate.getId(), "Meditation Duration", "15 minutes");
        entry1.setOrderIndex(0);
        entryRepository.save(entry1);

        Entry entry2 = new Entry(testUser, EntryType.CHECKBOX, EntrySubjectType.TEMPLATE,
                testTemplate.getId(), "Tasks", "[{\"label\":\"Drink water\",\"checked\":false}]");
        entry2.setOrderIndex(1);
        entryRepository.save(entry2);
    }

    // ==================== DAILY FREQUENCY ====================

    @Test
    void testPopulateDaily_KeepAll() {
        // Create daily schedule rule
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 1, 5));
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        // Populate calendar
        List<Event> events = templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        assertEquals(5, events.size());
        
        // Verify first event
        Event firstEvent = events.get(0);
        assertEquals("Morning Routine", firstEvent.getTitle());
        assertEquals("Daily morning routine", firstEvent.getNote());
        assertEquals(LocalDate.of(2024, 1, 1), firstEvent.getDateTime().toLocalDate());
        assertEquals(testTemplate.getId(), firstEvent.getTemplateId());

        // Verify entries were copied
        List<Entry> eventEntries = entryRepository.findBySubjectTypeAndSubjectIdOrderByOrderIndexAsc(
                EntrySubjectType.EVENT, firstEvent.getId());
        assertEquals(2, eventEntries.size());
        assertEquals("Meditation Duration", eventEntries.get(0).getLabel());
        assertEquals("15 minutes", eventEntries.get(0).getValue());
    }

    @Test
    void testPopulateDaily_DeleteAll() {
        // Create initial rule and populate
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 1, 5));
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        assertEquals(5, eventRepository.findByTemplateId(testTemplate.getId()).size());

        // Repopulate with DELETE_ALL
        List<Event> events = templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.DELETE_ALL
        );

        // Should still have 5 events (deleted old, created new)
        assertEquals(5, events.size());
        assertEquals(5, eventRepository.findByTemplateId(testTemplate.getId()).size());
    }

    @Test
    void testPopulateDaily_DeleteFuture() {
        // Create events in the past and future
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.now().minusDays(5));
        rule.setEndDate(LocalDate.now().plusDays(5));
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        // First population
        templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        int initialCount = eventRepository.findByTemplateId(testTemplate.getId()).size();
        assertTrue(initialCount > 5); // Should have past and future events

        // Repopulate with DELETE_FUTURE
        templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.DELETE_FUTURE
        );

        // Verify past events still exist
        List<Event> pastEvents = eventRepository.findByTemplateIdAndDateTimeBetween(
                testTemplate.getId(),
                LocalDate.now().minusDays(5).atStartOfDay(),
                LocalDateTime.now()
        );
        assertTrue(pastEvents.size() >= 5);
    }

    // ==================== WEEKLY FREQUENCY ====================

    @Test
    void testPopulateWeekly_MultipleDays() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1)); // Monday
        rule.setEndDate(LocalDate.of(2024, 1, 14));
        rule.setDaysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        List<Event> events = templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        // Week 1: Mon(1), Wed(3), Fri(5)
        // Week 2: Mon(8), Wed(10), Fri(12)
        assertEquals(6, events.size());

        // Verify correct days
        assertTrue(events.stream().anyMatch(e -> 
                e.getDateTime().toLocalDate().equals(LocalDate.of(2024, 1, 1))));
        assertTrue(events.stream().anyMatch(e -> 
                e.getDateTime().toLocalDate().equals(LocalDate.of(2024, 1, 3))));
        assertTrue(events.stream().anyMatch(e -> 
                e.getDateTime().toLocalDate().equals(LocalDate.of(2024, 1, 5))));
    }

    // ==================== MONTHLY FREQUENCY ====================

    @Test
    void testPopulateMonthly_DayOfMonth() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 4, 30));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(15);
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        List<Event> events = templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        assertEquals(4, events.size());
        assertEquals(LocalDate.of(2024, 1, 15), events.get(0).getDateTime().toLocalDate());
        assertEquals(LocalDate.of(2024, 2, 15), events.get(1).getDateTime().toLocalDate());
        assertEquals(LocalDate.of(2024, 3, 15), events.get(2).getDateTime().toLocalDate());
        assertEquals(LocalDate.of(2024, 4, 15), events.get(3).getDateTime().toLocalDate());
    }

    @Test
    void testPopulateMonthly_FirstMonday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FIRST);
        rule.setWeekday(DayOfWeek.MONDAY);
        rule.setActive(true);
        scheduleRuleRepository.save(rule);

        List<Event> events = templateService.populateCalendarFromTemplate(
                testTemplate.getId(),
                testCalendar.getId(),
                EventDeleteStrategy.KEEP_ALL
        );

        assertEquals(3, events.size());
        // Verify all are Mondays
        events.forEach(event -> 
                assertEquals(DayOfWeek.MONDAY, event.getDateTime().getDayOfWeek()));
    }

    // ==================== ERROR CASES ====================

    @Test
    void testPopulate_NoActiveRule_ThrowsException() {
        // Create inactive rule
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(testUser);
        rule.setTemplate(testTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 1, 5));
        rule.setActive(false); // Inactive
        scheduleRuleRepository.save(rule);

        assertThrows(RuntimeException.class, () -> 
                templateService.populateCalendarFromTemplate(
                        testTemplate.getId(),
                        testCalendar.getId(),
                        EventDeleteStrategy.KEEP_ALL
                ));
    }

    @Test
    void testPopulate_NoRule_ThrowsException() {
        // No rule created
        assertThrows(RuntimeException.class, () -> 
                templateService.populateCalendarFromTemplate(
                        testTemplate.getId(),
                        testCalendar.getId(),
                        EventDeleteStrategy.KEEP_ALL
                ));
    }
}