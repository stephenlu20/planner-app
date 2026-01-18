package com.planner.api.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "schedule_rules")
public class ScheduleRule {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleFrequency frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "schedule_rule_weekdays",
        joinColumns = @JoinColumn(name = "schedule_rule_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private MonthlyPatternType monthlyPatternType;

    private Integer dayOfMonth;

    @Enumerated(EnumType.STRING)
    private WeekOrdinal weekOrdinal;

    @Enumerated(EnumType.STRING)
    private DayOfWeek weekday;

    @Column(nullable = false)
    private boolean active = true;

    protected ScheduleRule() {}

    // --- Getters ---

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Template getTemplate() {
        return template;
    }

    public ScheduleFrequency getFrequency() {
        return frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public MonthlyPatternType getMonthlyPatternType() {
        return monthlyPatternType;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public WeekOrdinal getWeekOrdinal() {
        return weekOrdinal;
    }

    public DayOfWeek getWeekday() {
        return weekday;
    }

    public boolean isActive() {
        return active;
    }

    // --- Setters ---

    public void setUser(User user) {
        this.user = user;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public void setFrequency(ScheduleFrequency frequency) {
        this.frequency = frequency;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setMonthlyPatternType(MonthlyPatternType monthlyPatternType) {
        this.monthlyPatternType = monthlyPatternType;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setWeekOrdinal(WeekOrdinal weekOrdinal) {
        this.weekOrdinal = weekOrdinal;
    }

    public void setWeekday(DayOfWeek weekday) {
        this.weekday = weekday;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
