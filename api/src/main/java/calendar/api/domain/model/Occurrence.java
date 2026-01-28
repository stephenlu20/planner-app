package calendar.api.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public final class Occurrence {

    private final UUID eventId;
    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;
    private final boolean overridden;

    public Occurrence(UUID eventId, ZonedDateTime startTime, ZonedDateTime endTime, boolean overridden) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.overridden = overridden;
    }

    public UUID eventId() { return eventId; }
    public ZonedDateTime startTime() { return startTime; }
    public ZonedDateTime endTime() { return endTime; }
    public boolean isOverridden() { return overridden; }
}