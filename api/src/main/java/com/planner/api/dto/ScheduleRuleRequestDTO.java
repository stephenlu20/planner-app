package com.planner.api.dto;

import com.planner.api.entity.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class ScheduleRuleRequestDTO {

    private Long userId;
    private UUID templateId;

    private ScheduleFrequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    private Set<DayOfWeek> daysOfWeek;

    private MonthlyPatternType monthlyPatternType;
    private Integer dayOfMonth;
    private WeekOrdinal weekOrdinal;
    private DayOfWeek weekday;

    private boolean active = true;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public ScheduleFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(ScheduleFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public MonthlyPatternType getMonthlyPatternType() {
        return monthlyPatternType;
    }

    public void setMonthlyPatternType(MonthlyPatternType monthlyPatternType) {
        this.monthlyPatternType = monthlyPatternType;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
