package com.planner.api.dto;

import com.planner.api.entity.MonthlyPatternType;
import com.planner.api.entity.ScheduleFrequency;
import com.planner.api.entity.ScheduleRule;
import com.planner.api.entity.WeekOrdinal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class SchedulePreviewResponseDTO {

    private List<LocalDate> dates;
    private int count;
    private LocalDate firstDate;
    private LocalDate lastDate;

    // Schedule rule information
    private ScheduleFrequency frequency;
    private Set<DayOfWeek> daysOfWeek;
    private MonthlyPatternType monthlyPattern;
    private Integer dayOfMonth;
    private WeekOrdinal weekOrdinal;
    private DayOfWeek weekday;

    public SchedulePreviewResponseDTO() {
    }

    public SchedulePreviewResponseDTO(List<LocalDate> dates) {
        this.dates = dates;
        this.count = dates.size();
        this.firstDate = dates.isEmpty() ? null : dates.get(0);
        this.lastDate = dates.isEmpty() ? null : dates.get(dates.size() - 1);
    }

    // Enhanced constructor that includes schedule rule information
    public SchedulePreviewResponseDTO(List<LocalDate> dates, ScheduleRule rule) {
        this.dates = dates;
        this.count = dates.size();
        this.firstDate = dates.isEmpty() ? null : dates.get(0);
        this.lastDate = dates.isEmpty() ? null : dates.get(dates.size() - 1);

        if (rule != null) {
            this.frequency = rule.getFrequency();

            if (rule.getFrequency() == ScheduleFrequency.WEEKLY) {
                this.daysOfWeek = rule.getDaysOfWeek();
            }

            if (rule.getFrequency() == ScheduleFrequency.MONTHLY) {
                this.monthlyPattern = rule.getMonthlyPatternType();
                this.dayOfMonth = rule.getDayOfMonth();
                this.weekOrdinal = rule.getWeekOrdinal();
                this.weekday = rule.getWeekday();
            }
        }
    }

    // Getters and Setters
    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public ScheduleFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(ScheduleFrequency frequency) {
        this.frequency = frequency;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public MonthlyPatternType getMonthlyPattern() {
        return monthlyPattern;
    }

    public void setMonthlyPattern(MonthlyPatternType monthlyPattern) {
        this.monthlyPattern = monthlyPattern;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public WeekOrdinal getWeekOrdinal() {
        return weekOrdinal;
    }

    public void setWeekOrdinal(WeekOrdinal weekOrdinal) {
        this.weekOrdinal = weekOrdinal;
    }

    public DayOfWeek getWeekday() {
        return weekday;
    }

    public void setWeekday(DayOfWeek weekday) {
        this.weekday = weekday;
    }
}