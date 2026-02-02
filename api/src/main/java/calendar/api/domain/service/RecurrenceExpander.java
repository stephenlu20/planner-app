package calendar.api.domain.service;

import calendar.api.domain.model.Event;
import calendar.api.domain.model.Occurrence;
import calendar.api.domain.model.RecurrenceRule;
import calendar.api.domain.value.DayOfWeekOrdinal;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RecurrenceExpander {

    public List<Occurrence> expand(
            Event event,
            ZonedDateTime rangeStart,
            ZonedDateTime rangeEnd
    ) {
        List<Occurrence> results = new ArrayList<>();

        RecurrenceRule rule = event.recurrenceRule();

        // Non-recurring shortcut
        if (rule == null) {
            if (overlaps(event.startTime(), event.endTime(), rangeStart, rangeEnd)) {
                results.add(new Occurrence(
                        event.id(),
                        event.startTime(),
                        event.endTime()
                ));
            }
            return results;
        }

        ZonedDateTime cursor = event.startTime();
        long durationMinutes = ChronoUnit.MINUTES.between(event.startTime(), event.endTime());
        int generated = 0;

        while (shouldContinue(cursor, rule, rangeEnd, generated)) {

            if (matchesByDay(cursor, rule.byDay())) {

                ZonedDateTime occurrenceEnd = cursor.plusMinutes(durationMinutes);

                if (overlaps(cursor, occurrenceEnd, rangeStart, rangeEnd)) {
                    results.add(new Occurrence(
                            event.id(),
                            cursor,
                            occurrenceEnd
                    ));
                }

                generated++;
            }

            cursor = advance(cursor, rule);
        }

        return results;
    }

    // ---------- helpers ----------

    private boolean shouldContinue(
            ZonedDateTime cursor,
            RecurrenceRule rule,
            ZonedDateTime rangeEnd,
            int generated
    ) {
        if (cursor.isAfter(rangeEnd)) return false;
        if (rule.until() != null && cursor.isAfter(rule.until())) return false;
        return rule.count() == null || generated < rule.count();
    }

    private boolean matchesByDay(
            ZonedDateTime dateTime,
            Set<DayOfWeekOrdinal> byDay
    ) {
        if (byDay == null || byDay.isEmpty()) return true;

        for (DayOfWeekOrdinal dwo : byDay) {
            if (dwo.matches(dateTime)) {
                return true;
            }
        }
        return false;
    }

    private ZonedDateTime advance(ZonedDateTime cursor, RecurrenceRule rule) {
        return switch (rule.frequency()) {
            case DAILY -> cursor.plusDays(rule.interval());
            case WEEKLY -> cursor.plusWeeks(rule.interval());
            case MONTHLY -> cursor.plusMonths(rule.interval());
            case YEARLY -> cursor.plusYears(rule.interval());
        };
    }

    private boolean overlaps(
            ZonedDateTime start1,
            ZonedDateTime end1,
            ZonedDateTime start2,
            ZonedDateTime end2
    ) {
        return !end1.isBefore(start2) && !start1.isAfter(end2);
    }
}
