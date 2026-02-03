package calendar.api.domain.service;

import calendar.api.domain.model.Event;
import calendar.api.domain.model.Frequency;
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

        if (rule == null) {
            if (overlaps(event.startTime(), event.endTime(), rangeStart, rangeEnd)) {
                results.add(new Occurrence(event.id(), event.startTime(), event.endTime()));
            }
            return results;
        }

        long durationMinutes = ChronoUnit.MINUTES.between(event.startTime(), event.endTime());
        
        // Special handling for WEEKLY + BYDAY
        if (rule.frequency() == Frequency.WEEKLY && !rule.byDay().isEmpty()) {
            return expandWeeklyByDay(event, rule, rangeStart, rangeEnd, durationMinutes);
        }

        // Standard expansion for other cases
        ZonedDateTime cursor = event.startTime();
        int generated = 0;

        while (shouldContinue(cursor, rule, rangeEnd, generated)) {
            if (matchesByDay(cursor, rule.byDay())) {
                ZonedDateTime occurrenceEnd = cursor.plusMinutes(durationMinutes);
                if (overlaps(cursor, occurrenceEnd, rangeStart, rangeEnd)) {
                    results.add(new Occurrence(event.id(), cursor, occurrenceEnd));
                }
                generated++;
            }
            cursor = advance(cursor, rule);
        }

        return results;
    }

    private List<Occurrence> expandWeeklyByDay(
            Event event,
            RecurrenceRule rule,
            ZonedDateTime rangeStart,
            ZonedDateTime rangeEnd,
            long durationMinutes
    ) {
        List<Occurrence> results = new ArrayList<>();
        
        // Start from the beginning of the week containing event start
        ZonedDateTime weekCursor = event.startTime();
        int weekCount = 0;
        
        while (true) {
            // For this week, generate occurrences for each BYDAY
            int occurrencesThisWeek = 0;
            
            for (DayOfWeekOrdinal dwo : rule.byDay()) {
                // Find the date for this day of week in the current week
                ZonedDateTime dayInWeek = weekCursor.with(dwo.dayOfWeek());
                
                // Ensure it's not before the event start
                if (dayInWeek.isBefore(event.startTime())) {
                    continue;
                }
                
                ZonedDateTime occurrenceEnd = dayInWeek.plusMinutes(durationMinutes);
                
                if (overlaps(dayInWeek, occurrenceEnd, rangeStart, rangeEnd)) {
                    results.add(new Occurrence(event.id(), dayInWeek, occurrenceEnd));
                }
                
                occurrencesThisWeek++;
            }
            
            // Check stopping conditions
            weekCount++;
            weekCursor = weekCursor.plusWeeks(rule.interval());
            
            if (weekCursor.isAfter(rangeEnd)) break;
            if (rule.until() != null && weekCursor.isAfter(rule.until())) break;
            if (rule.count() != null && results.size() >= rule.count()) break;
        }
        
        // Trim to exact count if specified
        if (rule.count() != null && results.size() > rule.count()) {
            return results.subList(0, rule.count());
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
            case MONTHLY -> advanceMonthly(cursor, rule);
            case YEARLY -> cursor.plusYears(rule.interval());
        };
    }

    private ZonedDateTime advanceMonthly(ZonedDateTime cursor, RecurrenceRule rule) {
        if (rule.byDay() == null || rule.byDay().isEmpty()) {
            return cursor.plusMonths(rule.interval());
        }
        
        // For MONTHLY+BYDAY, move to next month and find first matching day
        ZonedDateTime nextMonth = cursor.plusMonths(rule.interval());
        ZonedDateTime firstOfMonth = nextMonth.withDayOfMonth(1).withHour(cursor.getHour())
                .withMinute(cursor.getMinute()).withSecond(cursor.getSecond());
        
        // Find first matching day in the month
        for (int day = 1; day <= nextMonth.toLocalDate().lengthOfMonth(); day++) {
            ZonedDateTime candidate = firstOfMonth.withDayOfMonth(day);
            if (matchesByDay(candidate, rule.byDay())) {
                return candidate;
            }
        }
        
        // No match found, skip this month
        return nextMonth.plusMonths(1).withDayOfMonth(1);
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
