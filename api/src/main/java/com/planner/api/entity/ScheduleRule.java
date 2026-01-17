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

    /* --------------------
     * Ownership
     * -------------------- */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    /* --------------------
     * Core recurrence
     * -------------------- */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleFrequency frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate; // null = repeat indefinitely

    /* --------------------
     * Weekly configuration
     * -------------------- */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "schedule_rule_weekdays",
        joinColumns = @JoinColumn(name = "schedule_rule_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    /* --------------------
     * Monthly configuration
     * -------------------- */
    @Enumerated(EnumType.STRING)
    private MonthlyPatternType monthlyPatternType;

    // For DAY_OF_MONTH
    private Integer dayOfMonth; // 1–31

    // For NTH_WEEKDAY_OF_MONTH
    @Enumerated(EnumType.STRING)
    private WeekOrdinal weekOrdinal;

    @Enumerated(EnumType.STRING)
    private DayOfWeek weekday;

    /* --------------------
     * Metadata
     * -------------------- */
    @Column(nullable = false)
    private boolean active = true;

    protected ScheduleRule() {}
}
