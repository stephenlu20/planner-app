package com.planner.api.mapper;

import com.planner.api.dto.ScheduleRuleRequestDTO;
import com.planner.api.dto.ScheduleRuleResponseDTO;
import com.planner.api.entity.ScheduleRule;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;

public class ScheduleRuleMapper {

    public static ScheduleRule toEntity(
            ScheduleRuleRequestDTO dto,
            User user,
            Template template
    ) {
        ScheduleRule rule = new ScheduleRule();

        rule.setUser(user);
        rule.setTemplate(template);
        rule.setFrequency(dto.getFrequency());
        rule.setStartDate(dto.getStartDate());
        rule.setEndDate(dto.getEndDate());

        rule.setDaysOfWeek(dto.getDaysOfWeek());

        rule.setMonthlyPatternType(dto.getMonthlyPatternType());
        rule.setDayOfMonth(dto.getDayOfMonth());
        rule.setWeekOrdinal(dto.getWeekOrdinal());
        rule.setWeekday(dto.getWeekday());

        rule.setActive(dto.isActive());

        return rule;
    }

    public static ScheduleRuleResponseDTO toDTO(ScheduleRule rule) {
        ScheduleRuleResponseDTO dto = new ScheduleRuleResponseDTO();

        dto.setId(rule.getId());
        dto.setUserId(rule.getUser().getId());
        dto.setTemplateId(rule.getTemplate().getId());

        dto.setFrequency(rule.getFrequency());
        dto.setStartDate(rule.getStartDate());
        dto.setEndDate(rule.getEndDate());

        dto.setDaysOfWeek(rule.getDaysOfWeek());

        dto.setMonthlyPatternType(rule.getMonthlyPatternType());
        dto.setDayOfMonth(rule.getDayOfMonth());
        dto.setWeekOrdinal(rule.getWeekOrdinal());
        dto.setWeekday(rule.getWeekday());

        dto.setActive(rule.isActive());

        return dto;
    }
}
