package calendar.api.domain.value;

import java.time.DayOfWeek;
import java.util.Objects;

public final class DayOfWeekOrdinal {

    private final DayOfWeek dayOfWeek;
    private final Integer ordinal; // e.g., 1 = first, -1 = last

    public DayOfWeekOrdinal(DayOfWeek dayOfWeek, Integer ordinal) {
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek);
        this.ordinal = ordinal;
    }

    public DayOfWeek dayOfWeek() { return dayOfWeek; }
    public Integer ordinal() { return ordinal; }
}