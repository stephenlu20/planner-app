package com.planner.api.service;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.repository.TemplateRepository;
import com.planner.api.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TemplateServiceTest {

    private TemplateRepository templateRepository;
    private ScheduleRuleService scheduleRuleService;
    private ScheduleRuleEventGenerator eventGenerator;
    private EventRepository eventRepository;
    private EntryService entryService;
    private CalendarService calendarService;
    private TemplateService templateService;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        templateRepository = mock(TemplateRepository.class);
        scheduleRuleService = mock(ScheduleRuleService.class);
        eventGenerator = mock(ScheduleRuleEventGenerator.class);
        eventRepository = mock(EventRepository.class);
        entryService = mock(EntryService.class);
        calendarService = mock(CalendarService.class);
        
        templateService = new TemplateService(
            templateRepository,
            scheduleRuleService,
            eventGenerator,
            eventRepository,
            entryService,
            calendarService
        );

        dummyUser = new User("Test User");
    }

    @Test
    void testCreateTemplate() {
        Template template = new Template();
        template.setName("Test Template");
        template.setNote("This is a test note");

        when(templateRepository.save(template)).thenReturn(template);

        Template saved = templateService.createTemplate(template, dummyUser, null);
        assertNotNull(saved);
        assertEquals("Test Template", saved.getName());
        assertEquals("This is a test note", saved.getNote());
        assertEquals(dummyUser, saved.getOwner());
        verify(templateRepository, times(1)).save(template);
        verify(scheduleRuleService, never()).create(any());
    }

    @Test
    void testGetTemplate_NotFound() {
        UUID id = UUID.randomUUID();
        when(templateRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> templateService.getTemplate(id));
    }

    @Test
    void testGetTemplatesByUser() {
        Template template1 = new Template();
        template1.setName("Template 1");
        template1.setOwner(dummyUser);

        Template template2 = new Template();
        template2.setName("Template 2");
        template2.setOwner(dummyUser);

        when(templateRepository.findAllByOwnerId(dummyUser.getId()))
                .thenReturn(List.of(template1, template2));

        List<Template> list = templateService.getTemplatesByUser(dummyUser.getId());
        assertEquals(2, list.size());
        assertEquals("Template 1", list.get(0).getName());
        assertEquals("Template 2", list.get(1).getName());
    }

    @Test
    void testUpdateTemplate() {
        UUID id = UUID.randomUUID();

        Template existing = new Template();
        existing.setId(id);
        existing.setName("Old Template");
        existing.setNote("Old note");
        existing.setOwner(dummyUser);

        Template updated = new Template();
        updated.setName("Updated Template");
        updated.setNote("Updated note");

        when(templateRepository.findById(id)).thenReturn(Optional.of(existing));
        when(templateRepository.save(any(Template.class))).thenReturn(existing);
        when(scheduleRuleService.getByTemplate(any())).thenReturn(List.of());

        Template result = templateService.updateTemplate(id, updated, null);
        assertEquals("Updated Template", result.getName());
        assertEquals("Updated note", result.getNote());
        verify(scheduleRuleService, never()).create(any());
    }

    @Test
    void testDeleteTemplate() {
        UUID id = UUID.randomUUID();
        doNothing().when(templateRepository).deleteById(id);
        templateService.deleteTemplate(id);
        verify(templateRepository, times(1)).deleteById(id);
    }
}