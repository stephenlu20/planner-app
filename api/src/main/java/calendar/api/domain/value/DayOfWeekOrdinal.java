package calendar.api.domain.value;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
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

    public boolean matches(ZonedDateTime dateTime) {
        if (dateTime.getDayOfWeek() != dayOfWeek) {
            return false;
        }

        // No ordinal → every matching weekday is valid
        if (ordinal == null) {
            return true;
        }

        int dayOfMonth = dateTime.getDayOfMonth();

        // 1-based week index of this weekday in the month
        int weekIndex = (dayOfMonth - 1) / 7 + 1;

        if (ordinal > 0) {
            return weekIndex == ordinal;
        }

        // ordinal < 0 → count from end of month (e.g. -1 = last)
        int daysInMonth = dateTime.toLocalDate().lengthOfMonth();
        int lastWeekIndex = (daysInMonth - 1) / 7 + 1;

        return weekIndex == lastWeekIndex;
    }
}