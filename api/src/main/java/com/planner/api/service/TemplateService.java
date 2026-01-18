package com.planner.api.service;

import com.planner.api.entity.*;
import com.planner.api.mapper.ScheduleRuleMapper;
import com.planner.api.dto.ScheduleRuleRequestDTO;
import com.planner.api.repository.TemplateRepository;
import com.planner.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ScheduleRuleService scheduleRuleService;
    private final ScheduleRuleEventGenerator eventGenerator;
    private final EventRepository eventRepository;
    private final EntryService entryService;
    private final CalendarService calendarService;

    public Template createTemplate(Template template, User owner, ScheduleRuleRequestDTO ruleDto) {
        template.setOwner(owner);
        Template saved = templateRepository.save(template);

        if (ruleDto != null) {
            ScheduleRule rule = ScheduleRuleMapper.toEntity(ruleDto, owner, saved);
            scheduleRuleService.create(rule);
        }

        return saved;
    }

    public Template getTemplate(UUID id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public List<Template> getTemplatesByUser(Long userId) {
        return templateRepository.findAllByOwnerId(userId);
    }

    public Template updateTemplate(UUID id, Template updatedTemplate, ScheduleRuleRequestDTO ruleDto) {
        Template existing = getTemplate(id);
        existing.setName(updatedTemplate.getName());
        existing.setNote(updatedTemplate.getNote());
        Template saved = templateRepository.save(existing);

        if (ruleDto != null) {
            // MVP: update first rule if exists, otherwise create
            List<ScheduleRule> rules = scheduleRuleService.getByTemplate(saved);
            if (rules.isEmpty()) {
                ScheduleRule newRule = ScheduleRuleMapper.toEntity(ruleDto, saved.getOwner(), saved);
                scheduleRuleService.create(newRule);
            } else {
                ScheduleRule rule = rules.get(0);
                rule.setFrequency(ruleDto.getFrequency());
                rule.setStartDate(ruleDto.getStartDate());
                rule.setEndDate(ruleDto.getEndDate());
                rule.setDaysOfWeek(ruleDto.getDaysOfWeek());
                rule.setMonthlyPatternType(ruleDto.getMonthlyPatternType());
                rule.setDayOfMonth(ruleDto.getDayOfMonth());
                rule.setWeekOrdinal(ruleDto.getWeekOrdinal());
                rule.setWeekday(ruleDto.getWeekday());
                rule.setActive(ruleDto.isActive());
                scheduleRuleService.update(rule);
            }
        }

        return saved;
    }

    public void deleteTemplate(UUID id) {
        templateRepository.deleteById(id);
    }

    /**
     * Populate calendar with events from template's schedule rule
     */
    @Transactional
    public List<Event> populateCalendarFromTemplate(
            UUID templateId, 
            UUID calendarId,
            EventDeleteStrategy deleteStrategy
    ) {
        Template template = getTemplate(templateId);
        
        // Get active schedule rules for this template
        List<ScheduleRule> rules = scheduleRuleService.getByTemplate(template);
        if (rules.isEmpty() || !rules.get(0).isActive()) {
            throw new RuntimeException("No active schedule rule found for template");
        }
        
        ScheduleRule rule = rules.get(0);
        
        // Handle deletion based on strategy
        handleEventDeletion(templateId, calendarId, deleteStrategy, rule);
        
        // Generate dates from schedule rule
        List<LocalDate> dates = eventGenerator.generateEventDates(rule);
        
        // Get template entries to copy
        List<Entry> templateEntries = entryService.getEntriesBySubject(
            EntrySubjectType.TEMPLATE, 
            templateId
        );
        
        // Create events for each date
        List<Event> createdEvents = new ArrayList<>();
        User user = template.getOwner();
        Calendar calendar = calendarService.getCalendar(calendarId);
        
        int orderIndex = getNextOrderIndexForCalendar(calendarId);
        
        for (LocalDate date : dates) {
            // Create event
            LocalDateTime dateTime = date.atTime(LocalTime.MIDNIGHT);
            Event event = new Event(
                template.getName(),
                template.getNote(),
                dateTime,
                user,
                calendar,
                orderIndex++,
                templateId
            );
            
            Event saved = eventRepository.save(event);
            
            // Copy entries from template to event
            for (Entry templateEntry : templateEntries) {
                Entry eventEntry = new Entry(
                    user,
                    templateEntry.getType(),
                    EntrySubjectType.EVENT,
                    saved.getId(),
                    templateEntry.getLabel(),
                    templateEntry.getValue()
                );
                eventEntry.setNote(templateEntry.getNote());
                eventEntry.setOrderIndex(templateEntry.getOrderIndex());
                entryService.createEntry(eventEntry);
            }
            
            createdEvents.add(saved);
        }
        
        return createdEvents;
    }
    
    private void handleEventDeletion(
            UUID templateId, 
            UUID calendarId, 
            EventDeleteStrategy strategy,
            ScheduleRule rule
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ruleStart = rule.getStartDate().atStartOfDay();
        LocalDateTime ruleEnd = rule.getEndDate() != null 
            ? rule.getEndDate().atTime(23, 59, 59)
            : ruleStart.plusYears(1).minusDays(1).toLocalDate().atTime(23, 59, 59);
        
        switch (strategy) {
            case KEEP_ALL:
                // Do nothing
                break;
                
            case DELETE_FUTURE:
                // Delete events from now onwards within rule range
                eventRepository.deleteByTemplateIdAndCalendarIdAndDateTimeBetween(
                    templateId,
                    calendarId,
                    now,
                    ruleEnd
                );
                break;
                
            case DELETE_ALL:
                // Delete all events within rule range
                eventRepository.deleteByTemplateIdAndCalendarIdAndDateTimeBetween(
                    templateId,
                    calendarId,
                    ruleStart,
                    ruleEnd
                );
                break;
        }
    }
    
    private int getNextOrderIndexForCalendar(UUID calendarId) {
        List<Event> events = eventRepository.findByCalendarIdOrderByOrderIndexAsc(calendarId);
        return events.isEmpty() ? 0 : events.get(events.size() - 1).getOrderIndex() + 1;
    }
}