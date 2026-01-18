package com.planner.api.service;

import com.planner.api.entity.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleRuleValidator {

    public List<String> validate(ScheduleRule rule) {
        List<String> errors = new ArrayList<>();

        // Basic validations
        if (rule.getFrequency() == null) {
            errors.add("Frequency is required");
        }
        
        if (rule.getStartDate() == null) {
            errors.add("Start date is required");
        }

        if (rule.getStartDate() != null && rule.getEndDate() != null) {
            if (rule.getEndDate().isBefore(rule.getStartDate())) {
                errors.add("End date must be after start date");
            }
        }

        // Frequency-specific validations
        if (rule.getFrequency() != null) {
            switch (rule.getFrequency()) {
                case WEEKLY:
                    validateWeekly(rule, errors);
                    break;
                case MONTHLY:
                    validateMonthly(rule, errors);
                    break;
                case DAILY:
                    // No additional validation needed
                    break;
            }
        }

        return errors;
    }

    private void validateWeekly(ScheduleRule rule, List<String> errors) {
        Set<DayOfWeek> daysOfWeek = rule.getDaysOfWeek();
        
        // Days of week are optional - if not specified, will use start date's day
        if (daysOfWeek != null && daysOfWeek.isEmpty()) {
            // Empty set is fine, will default to start date's day
        }
    }

    private void validateMonthly(ScheduleRule rule, List<String> errors) {
        MonthlyPatternType patternType = rule.getMonthlyPatternType();
        
        if (patternType == null) {
            errors.add("Monthly pattern type is required for monthly frequency");
            return;
        }

        switch (patternType) {
            case DAY_OF_MONTH:
                validateDayOfMonth(rule, errors);
                break;
            case NTH_WEEKDAY_OF_MONTH:
                validateNthWeekday(rule, errors);
                break;
        }
    }

    private void validateDayOfMonth(ScheduleRule rule, List<String> errors) {
        Integer dayOfMonth = rule.getDayOfMonth();
        
        if (dayOfMonth == null) {
            errors.add("Day of month is required for DAY_OF_MONTH pattern");
            return;
        }

        if (dayOfMonth < 1 || dayOfMonth > 31) {
            errors.add("Day of month must be between 1 and 31");
        }
    }

    private void validateNthWeekday(ScheduleRule rule, List<String> errors) {
        WeekOrdinal ordinal = rule.getWeekOrdinal();
        DayOfWeek weekday = rule.getWeekday();

        if (ordinal == null) {
            errors.add("Week ordinal is required for NTH_WEEKDAY_OF_MONTH pattern");
        }

        if (weekday == null) {
            errors.add("Weekday is required for NTH_WEEKDAY_OF_MONTH pattern");
        }
    }

    public void validateAndThrow(ScheduleRule rule) {
        List<String> errors = validate(rule);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Schedule rule validation failed: " + String.join(", ", errors));
        }
    }
}