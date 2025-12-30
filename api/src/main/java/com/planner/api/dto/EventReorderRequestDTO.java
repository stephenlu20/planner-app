package com.planner.api.dto;

import java.util.List;
import java.util.UUID;

public class EventReorderRequestDTO {

    private UUID calendarId;
    private List<UUID> orderedEventIds;

    public EventReorderRequestDTO() {
    }

    public EventReorderRequestDTO(UUID calendarId, List<UUID> orderedEventIds) {
        this.calendarId = calendarId;
        this.orderedEventIds = orderedEventIds;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public List<UUID> getOrderedEventIds() {
        return orderedEventIds;
    }
}
