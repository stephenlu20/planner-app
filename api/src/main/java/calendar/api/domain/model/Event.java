package calendar.api.domain.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Event {

    private final UUID id;
    private final UUID calendarId;
    private final String title;
    private final String description;
    private final ZonedDateTime startTime; // anchor
    private final ZonedDateTime endTime;
    private final ZoneId timeZone;
    private final boolean allDay;
    private final EventStatus status;
    private final RecurrenceRule recurrenceRule; // nullable


    public Event(
        UUID id,
        UUID calendarId,
        String title,
        String description,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        ZoneId timeZone,
        boolean allDay,
        EventStatus status,
        RecurrenceRule recurrenceRule) {

        this.id = Objects.requireNonNull(id);
        this.calendarId = Objects.requireNonNull(calendarId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.startTime = Objects.requireNonNull(startTime);
        this.endTime = Objects.requireNonNull(endTime);
        this.timeZone = Objects.requireNonNull(timeZone);
        this.allDay = allDay;
        this.status = Objects.requireNonNull(status);
        this.recurrenceRule = recurrenceRule;
        }


    public UUID id() { return id; }
    public UUID calendarId() { return calendarId; }
    public String title() { return title; }
    public String description() { return description; }
    public ZonedDateTime startTime() { return startTime; }
    public ZonedDateTime endTime() { return endTime; }
    public ZoneId timeZone() { return timeZone; }
    public boolean isAllDay() { return allDay; }
    public EventStatus status() { return status; }
    public RecurrenceRule recurrenceRule() { return recurrenceRule; }
}