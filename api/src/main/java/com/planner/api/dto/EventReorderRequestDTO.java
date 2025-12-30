package com.planner.api.dto;

import java.util.List;
import java.util.UUID;

public class EventReorderRequestDTO {

    private UUID userId;
    private List<UUID> orderedEventIds;

    public EventReorderRequestDTO() {
        // default for JSON deserialization
    }

    public EventReorderRequestDTO(UUID userId, List<UUID> orderedEventIds) {
        this.userId = userId;
        this.orderedEventIds = orderedEventIds;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<UUID> getOrderedEventIds() {
        return orderedEventIds;
    }
}
