package calendar.api.domain.model;

import calendar.api.domain.value.DayOfWeekOrdinal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.Objects;

public final class RecurrenceRule {

    private final Frequency frequency;
    private final int interval;
    private final Integer count;
    private final ZonedDateTime until;
    private final Set<DayOfWeekOrdinal> byDay;

    public RecurrenceRule(
            Frequency frequency,
            int interval,
            Integer count,
            ZonedDateTime until,
            Set<DayOfWeekOrdinal> byDay
    ) {
        this.frequency = Objects.requireNonNull(frequency);
        this.interval = interval <= 0 ? 1 : interval;

        if (count != null && until != null) {
            throw new IllegalArgumentException("RRULE cannot have both COUNT and UNTIL");
        }

        this.count = count;
        this.until = until;
        this.byDay = byDay == null ? Set.of() : Set.copyOf(byDay);
    }

    public Frequency frequency() { return frequency; }
    public int interval() { return interval; }
    public Integer count() { return count; }
    public ZonedDateTime until() { return until; }
    public Set<DayOfWeekOrdinal> byDay() { return byDay; }
}
