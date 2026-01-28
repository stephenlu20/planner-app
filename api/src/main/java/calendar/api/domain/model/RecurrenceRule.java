package calendar.api.domain.model;

import calendar.api.domain.value.DayOfWeekOrdinal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.Objects;

public final class RecurrenceRule {

    private final Frequency frequency;
    private final int interval;
    private final Integer count; // optional
    private final ZonedDateTime until; // optional
    private final Set<DayOfWeekOrdinal> byDay; // optional


    public RecurrenceRule(
        Frequency frequency,
        int interval,
        Integer count,
        ZonedDateTime until,
        Set<DayOfWeekOrdinal> byDay) {

        this.frequency = Objects.requireNonNull(frequency);
        this.interval = interval <= 0 ? 1 : interval;
        this.count = count;
        this.until = until;
        this.byDay = byDay;
        }


    public Frequency frequency() { return frequency; }
    public int interval() { return interval; }
    public Integer count() { return count; }
    public ZonedDateTime until() { return until; }
    public Set<DayOfWeekOrdinal> byDay() { return byDay; }
}
