package com.planner.api.service;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.mapper.ScheduleRuleMapper;
import com.planner.api.entity.ScheduleRule;
import com.planner.api.dto.ScheduleRuleRequestDTO;
import com.planner.api.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ScheduleRuleService scheduleRuleService;

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
}
