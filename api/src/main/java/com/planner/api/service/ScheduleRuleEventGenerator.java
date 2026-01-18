package com.planner.api.service;

import com.planner.api.entity.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleRuleEventGenerator {

    /**
     * Generate event dates based on schedule rule configuration
     */
    public List<LocalDate> generateEventDates(ScheduleRule rule) {
        List<LocalDate> dates = new ArrayList<>();
        
        LocalDate start = rule.getStartDate();
        LocalDate end = rule.getEndDate() != null 
            ? rule.getEndDate() 
            : start.plusYears(1).minusDays(1); // Default 1 year cap (inclusive)
        
        LocalDate current = start;
        
        switch (rule.getFrequency()) {
            case DAILY:
                while (!current.isAfter(end)) {
                    dates.add(current);
                    current = current.plusDays(1);
                }
                break;
                
            case WEEKLY:
                while (!current.isAfter(end)) {
                    dates.addAll(generateWeeklyDates(current, end, rule.getDaysOfWeek()));
                    current = current.plusWeeks(1);
                }
                break;
                
            case MONTHLY:
                while (!current.isAfter(end)) {
                    LocalDate monthlyDate = generateMonthlyDate(current, rule);
                    if (monthlyDate != null && !monthlyDate.isAfter(end)) {
                        dates.add(monthlyDate);
                    }
                    current = current.plusMonths(1);
                }
                break;
        }
        
        return dates;
    }
    
    private List<LocalDate> generateWeeklyDates(LocalDate weekStart, LocalDate end, Set<DayOfWeek> daysOfWeek) {
        List<LocalDate> dates = new ArrayList<>();
        
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            // If no days specified, use the start date's day of week
            dates.add(weekStart);
            return dates;
        }
        
        for (DayOfWeek day : daysOfWeek) {
            LocalDate date = weekStart.with(TemporalAdjusters.nextOrSame(day));
            if (!date.isAfter(end) && !date.isBefore(weekStart)) {
                dates.add(date);
            }
        }
        
        return dates;
    }
    
    private LocalDate generateMonthlyDate(LocalDate month, ScheduleRule rule) {
        if (rule.getMonthlyPatternType() == MonthlyPatternType.DAY_OF_MONTH) {
            // e.g., 15th of each month
            Integer dayOfMonth = rule.getDayOfMonth();
            if (dayOfMonth == null) return null;
            
            int lastDay = month.lengthOfMonth();
            int targetDay = Math.min(dayOfMonth, lastDay);
            
            return month.withDayOfMonth(targetDay);
            
        } else if (rule.getMonthlyPatternType() == MonthlyPatternType.NTH_WEEKDAY_OF_MONTH) {
            // e.g., first Monday of each month
            WeekOrdinal ordinal = rule.getWeekOrdinal();
            DayOfWeek weekday = rule.getWeekday();
            
            if (ordinal == null || weekday == null) return null;
            
            return getNthWeekdayOfMonth(month, ordinal, weekday);
        }
        
        return null;
    }
    
    private LocalDate getNthWeekdayOfMonth(LocalDate month, WeekOrdinal ordinal, DayOfWeek weekday) {
        LocalDate firstOfMonth = month.withDayOfMonth(1);
        
        if (ordinal == WeekOrdinal.LAST) {
            // Find last occurrence
            LocalDate lastOfMonth = month.withDayOfMonth(month.lengthOfMonth());
            return lastOfMonth.with(TemporalAdjusters.lastInMonth(weekday));
        }
        
        // Find first occurrence
        LocalDate firstOccurrence = firstOfMonth.with(TemporalAdjusters.nextOrSame(weekday));
        
        // Add weeks based on ordinal
        int weeksToAdd = switch (ordinal) {
            case FIRST -> 0;
            case SECOND -> 1;
            case THIRD -> 2;
            case FOURTH -> 3;
            default -> 0;
        };
        
        LocalDate result = firstOccurrence.plusWeeks(weeksToAdd);
        
        // Ensure we're still in the same month
        return result.getMonth() == month.getMonth() ? result : null;
    }
}