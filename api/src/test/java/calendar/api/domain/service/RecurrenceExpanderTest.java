package calendar.api.domain.service;

import calendar.api.domain.model.*;
import calendar.api.domain.value.DayOfWeekOrdinal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RecurrenceExpanderTest {

    private RecurrenceExpander expander;
    private static final ZoneId UTC = ZoneId.of("UTC");

    @BeforeEach
    void setUp() {
        expander = new RecurrenceExpander();
    }

    // ===== NON-RECURRING =====

    @Test
    void nonRecurring_withinRange_returnsOneOccurrence() {
        Event event = createEvent(
                "2026-02-05T10:00:00Z",
                "2026-02-05T11:00:00Z",
                null
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        assertEquals(1, occurrences.size());
        assertEquals(event.startTime(), occurrences.get(0).startTime());
    }

    @Test
    void nonRecurring_outsideRange_returnsEmpty() {
        Event event = createEvent(
                "2026-03-05T10:00:00Z",
                "2026-03-05T11:00:00Z",
                null
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        assertTrue(occurrences.isEmpty());
    }

    // ===== DAILY RECURRENCE =====

    @Test
    void daily_interval1_count5_generatesFiveOccurrences() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.DAILY,
                1,
                5,
                null,
                null
        );

        Event event = createEvent(
                "2026-02-01T10:00:00Z",
                "2026-02-01T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        assertEquals(5, occurrences.size());
        assertEquals(parse("2026-02-01T10:00:00Z"), occurrences.get(0).startTime());
        assertEquals(parse("2026-02-05T10:00:00Z"), occurrences.get(4).startTime());
    }

    @Test
    void daily_interval2_until_generatesCorrectCount() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.DAILY,
                2,
                null,
                parse("2026-02-10T00:00:00Z"),
                null
        );

        Event event = createEvent(
                "2026-02-01T10:00:00Z",
                "2026-02-01T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        // Feb 1, 3, 5, 7, 9 = 5 occurrences
        assertEquals(5, occurrences.size());
        assertEquals(parse("2026-02-09T10:00:00Z"), occurrences.get(4).startTime());
    }

    // ===== WEEKLY RECURRENCE =====

    @Test
    void weekly_interval1_noByDay_generatesWeeklyOccurrences() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.WEEKLY,
                1,
                4,
                null,
                null
        );

        Event event = createEvent(
                "2026-02-02T10:00:00Z", // Monday
                "2026-02-02T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        assertEquals(4, occurrences.size());
        assertEquals(parse("2026-02-02T10:00:00Z"), occurrences.get(0).startTime());
        assertEquals(parse("2026-02-23T10:00:00Z"), occurrences.get(3).startTime());
    }

    @Test
    void weekly_withByDay_mondayWednesdayFriday() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.WEEKLY,
                1,
                null,
                parse("2026-02-15T23:59:59Z"),
                Set.of(
                        new DayOfWeekOrdinal(DayOfWeek.MONDAY, null),
                        new DayOfWeekOrdinal(DayOfWeek.WEDNESDAY, null),
                        new DayOfWeekOrdinal(DayOfWeek.FRIDAY, null)
                )
        );

        Event event = createEvent(
                "2026-02-02T10:00:00Z", // Monday
                "2026-02-02T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        // Feb 2 (Mon), 4 (Wed), 6 (Fri), 9 (Mon), 11 (Wed), 13 (Fri)
        assertEquals(6, occurrences.size());
    }

    // ===== MONTHLY RECURRENCE =====

    @Test
    void monthly_interval1_count3_generatesThreeMonths() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.MONTHLY,
                1,
                3,
                null,
                null
        );

        Event event = createEvent(
                "2026-02-05T10:00:00Z",
                "2026-02-05T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-04-30T23:59:59Z")
        );

        assertEquals(3, occurrences.size());
        assertEquals(parse("2026-02-05T10:00:00Z"), occurrences.get(0).startTime());
        assertEquals(parse("2026-03-05T10:00:00Z"), occurrences.get(1).startTime());
        assertEquals(parse("2026-04-05T10:00:00Z"), occurrences.get(2).startTime());
    }

    @Test
    void monthly_firstMonday_generatesCorrectOccurrences() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.MONTHLY,
                1,
                3,
                null,
                Set.of(new DayOfWeekOrdinal(DayOfWeek.MONDAY, 1))
        );

        Event event = createEvent(
                "2026-02-02T10:00:00Z", // First Monday of Feb
                "2026-02-02T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-04-30T23:59:59Z")
        );

        assertEquals(3, occurrences.size());
        assertEquals(parse("2026-02-02T10:00:00Z"), occurrences.get(0).startTime()); // Feb 2
        assertEquals(parse("2026-03-02T10:00:00Z"), occurrences.get(1).startTime()); // Mar 2
        assertEquals(parse("2026-04-06T10:00:00Z"), occurrences.get(2).startTime()); // Apr 6
    }

    // ===== EDGE CASES =====

    @Test
    void rangeBeforeEventStart_returnsEmpty() {
        Event event = createEvent(
                "2026-03-01T10:00:00Z",
                "2026-03-01T11:00:00Z",
                null
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-02-28T23:59:59Z")
        );

        assertTrue(occurrences.isEmpty());
    }

    @Test
    void count_stopsAtExactCount() {
        RecurrenceRule rule = new RecurrenceRule(
                Frequency.DAILY,
                1,
                3,
                null,
                null
        );

        Event event = createEvent(
                "2026-02-01T10:00:00Z",
                "2026-02-01T11:00:00Z",
                rule
        );

        List<Occurrence> occurrences = expander.expand(
                event,
                parse("2026-02-01T00:00:00Z"),
                parse("2026-12-31T23:59:59Z")
        );

        assertEquals(3, occurrences.size());
    }

    // ===== HELPERS =====

    private Event createEvent(String start, String end, RecurrenceRule rule) {
        return new Event(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test Event",
                "Description",
                parse(start),
                parse(end),
                UTC,
                false,
                EventStatus.CONFIRMED,
                rule
        );
    }

    private ZonedDateTime parse(String iso) {
        return ZonedDateTime.parse(iso);
    }
}