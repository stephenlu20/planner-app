package com.planner.api.service;

import com.planner.api.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleRuleEventGeneratorTest {

    private ScheduleRuleEventGenerator generator;
    private User dummyUser;
    private Template dummyTemplate;

    @BeforeEach
    void setUp() {
        generator = new ScheduleRuleEventGenerator();
        dummyUser = new User("Test User");
        dummyTemplate = new Template();
        dummyTemplate.setName("Test Template");
    }

    // ==================== DAILY FREQUENCY ====================

    @Test
    void testDailyFrequency_WithEndDate() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 1, 5));

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(5, dates.size());
        assertEquals(LocalDate.of(2024, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 1, 5), dates.get(4));
    }

    @Test
    void testDailyFrequency_WithoutEndDate_DefaultsToOneYear() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(null);

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Should generate 366 days (2024 is a leap year)
        assertEquals(366, dates.size());
        assertEquals(LocalDate.of(2024, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 12, 31), dates.get(365));
    }

    // ==================== WEEKLY FREQUENCY ====================

    @Test
    void testWeeklyFrequency_SingleDay() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1)); // Monday
        rule.setEndDate(LocalDate.of(2024, 1, 29));
        rule.setDaysOfWeek(Set.of(DayOfWeek.MONDAY));

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(5, dates.size()); // 5 Mondays
        assertEquals(LocalDate.of(2024, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 1, 29), dates.get(4));
    }

    @Test
    void testWeeklyFrequency_MultipleDays() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1)); // Monday
        rule.setEndDate(LocalDate.of(2024, 1, 14));
        rule.setDaysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Week 1: Mon(1), Wed(3), Fri(5)
        // Week 2: Mon(8), Wed(10), Fri(12)
        // Week 3: Mon(15) is after end date
        assertEquals(6, dates.size());
        assertTrue(dates.contains(LocalDate.of(2024, 1, 1)));
        assertTrue(dates.contains(LocalDate.of(2024, 1, 3)));
        assertTrue(dates.contains(LocalDate.of(2024, 1, 5)));
    }

    @Test
    void testWeeklyFrequency_NoDaysSpecified_UsesStartDateDay() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 3)); // Wednesday
        rule.setEndDate(LocalDate.of(2024, 1, 17));
        rule.setDaysOfWeek(Set.of());

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Should generate Wednesdays: 3, 10, 17
        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 3), dates.get(0));
        assertEquals(LocalDate.of(2024, 1, 10), dates.get(1));
        assertEquals(LocalDate.of(2024, 1, 17), dates.get(2));
    }

    // ==================== MONTHLY FREQUENCY - DAY OF MONTH ====================

    @Test
    void testMonthlyFrequency_DayOfMonth() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 4, 30));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(15);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(4, dates.size());
        assertEquals(LocalDate.of(2024, 1, 15), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 15), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 15), dates.get(2));
        assertEquals(LocalDate.of(2024, 4, 15), dates.get(3));
    }

    @Test
    void testMonthlyFrequency_DayOfMonth_HandlesShortMonths() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(31);

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Jan 31, Feb 29 (2024 is leap year), Mar 31
        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 31), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 29), dates.get(1)); // Capped at 29
        assertEquals(LocalDate.of(2024, 3, 31), dates.get(2));
    }

    @Test
    void testMonthlyFrequency_DayOfMonth_February29_NonLeapYear() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2023, 1, 1)); // 2023 is NOT a leap year
        rule.setEndDate(LocalDate.of(2023, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(30);

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Jan 30, Feb 28 (capped), Mar 30
        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2023, 1, 30), dates.get(0));
        assertEquals(LocalDate.of(2023, 2, 28), dates.get(1));
        assertEquals(LocalDate.of(2023, 3, 30), dates.get(2));
    }

    // ==================== MONTHLY FREQUENCY - NTH WEEKDAY ====================

    @Test
    void testMonthlyFrequency_FirstMonday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 4, 30));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FIRST);
        rule.setWeekday(DayOfWeek.MONDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(4, dates.size());
        assertEquals(LocalDate.of(2024, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 5), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 4), dates.get(2));
        assertEquals(LocalDate.of(2024, 4, 1), dates.get(3));
    }

    @Test
    void testMonthlyFrequency_SecondTuesday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.SECOND);
        rule.setWeekday(DayOfWeek.TUESDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 9), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 13), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 12), dates.get(2));
    }

    @Test
    void testMonthlyFrequency_ThirdFriday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.THIRD);
        rule.setWeekday(DayOfWeek.FRIDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 19), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 16), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 15), dates.get(2));
    }

    @Test
    void testMonthlyFrequency_FourthSaturday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FOURTH);
        rule.setWeekday(DayOfWeek.SATURDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 27), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 24), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 23), dates.get(2));
    }

    @Test
    void testMonthlyFrequency_LastSunday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setEndDate(LocalDate.of(2024, 3, 31));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.LAST);
        rule.setWeekday(DayOfWeek.SUNDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2024, 1, 28), dates.get(0));
        assertEquals(LocalDate.of(2024, 2, 25), dates.get(1));
        assertEquals(LocalDate.of(2024, 3, 31), dates.get(2));
    }

    // ==================== EDGE CASES ====================

    @Test
    void testSingleDayRange() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 15));
        rule.setEndDate(LocalDate.of(2024, 1, 15));

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(1, dates.size());
        assertEquals(LocalDate.of(2024, 1, 15), dates.get(0));
    }

    @Test
    void testEndDateBeforeStartDate_ReturnsEmpty() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 15));
        rule.setEndDate(LocalDate.of(2024, 1, 10));

        List<LocalDate> dates = generator.generateEventDates(rule);

        assertEquals(0, dates.size());
    }

    @Test
    void testMonthlyNthWeekday_FourthDoesNotExist_ReturnsNull() {
        // February 2024 only has 4 Fridays, asking for 5th should skip
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 2, 1));
        rule.setEndDate(LocalDate.of(2024, 2, 29));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FOURTH);
        rule.setWeekday(DayOfWeek.FRIDAY);

        List<LocalDate> dates = generator.generateEventDates(rule);

        // Feb 2024: 4th Friday is Feb 23
        assertEquals(1, dates.size());
        assertEquals(LocalDate.of(2024, 2, 23), dates.get(0));
    }
}