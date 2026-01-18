package com.planner.api.service;

import com.planner.api.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleRuleValidatorTest {

    private ScheduleRuleValidator validator;
    private User dummyUser;
    private Template dummyTemplate;

    @BeforeEach
    void setUp() {
        validator = new ScheduleRuleValidator();
        dummyUser = new User("Test User");
        dummyTemplate = new Template();
        dummyTemplate.setName("Test Template");
    }

    // ==================== BASIC VALIDATIONS ====================

    @Test
    void testValidDailyRule() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testMissingFrequency() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Frequency is required"));
    }

    @Test
    void testMissingStartDate() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Start date is required"));
    }

    @Test
    void testEndDateBeforeStartDate() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 15));
        rule.setEndDate(LocalDate.of(2024, 1, 10));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("End date must be after start date"));
    }

    // ==================== WEEKLY VALIDATIONS ====================

    @Test
    void testValidWeeklyRule() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setDaysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidWeeklyRuleWithoutDaysOfWeek() {
        // Should be valid - will default to start date's day
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.WEEKLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.isEmpty());
    }

    // ==================== MONTHLY - DAY OF MONTH ====================

    @Test
    void testValidMonthlyDayOfMonth() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(15);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testMonthlyMissingPatternType() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Monthly pattern type is required for monthly frequency"));
    }

    @Test
    void testMonthlyDayOfMonth_MissingDay() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Day of month is required for DAY_OF_MONTH pattern"));
    }

    @Test
    void testMonthlyDayOfMonth_InvalidDay_TooLow() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(0);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Day of month must be between 1 and 31"));
    }

    @Test
    void testMonthlyDayOfMonth_InvalidDay_TooHigh() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.DAY_OF_MONTH);
        rule.setDayOfMonth(32);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Day of month must be between 1 and 31"));
    }

    // ==================== MONTHLY - NTH WEEKDAY ====================

    @Test
    void testValidMonthlyNthWeekday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FIRST);
        rule.setWeekday(DayOfWeek.MONDAY);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testMonthlyNthWeekday_MissingOrdinal() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekday(DayOfWeek.MONDAY);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Week ordinal is required for NTH_WEEKDAY_OF_MONTH pattern"));
    }

    @Test
    void testMonthlyNthWeekday_MissingWeekday() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));
        rule.setMonthlyPatternType(MonthlyPatternType.NTH_WEEKDAY_OF_MONTH);
        rule.setWeekOrdinal(WeekOrdinal.FIRST);

        List<String> errors = validator.validate(rule);
        assertTrue(errors.contains("Weekday is required for NTH_WEEKDAY_OF_MONTH pattern"));
    }

    // ==================== VALIDATE AND THROW ====================

    @Test
    void testValidateAndThrow_Valid() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.DAILY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        assertDoesNotThrow(() -> validator.validateAndThrow(rule));
    }

    @Test
    void testValidateAndThrow_Invalid() {
        ScheduleRule rule = new ScheduleRule();
        rule.setUser(dummyUser);
        rule.setTemplate(dummyTemplate);
        rule.setFrequency(ScheduleFrequency.MONTHLY);
        rule.setStartDate(LocalDate.of(2024, 1, 1));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateAndThrow(rule)
        );

        assertTrue(exception.getMessage().contains("validation failed"));
    }
}