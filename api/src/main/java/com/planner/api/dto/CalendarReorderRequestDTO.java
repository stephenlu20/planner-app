package com.planner.api.dto;

import java.util.List;
import java.util.UUID;

public class CalendarReorderRequestDTO {

    private UUID userId;
    private List<UUID> orderedCalendarIds;

    public CalendarReorderRequestDTO() { }

    public CalendarReorderRequestDTO(UUID userId, List<UUID> orderedCalendarIds) {
        this.userId = userId;
        this.orderedCalendarIds = orderedCalendarIds;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<UUID> getOrderedCalendarIds() {
        return orderedCalendarIds;
    }
}
