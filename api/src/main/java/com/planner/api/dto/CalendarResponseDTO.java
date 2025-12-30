package com.planner.api.dto;

import java.util.UUID;

public class CalendarResponseDTO {

    private UUID id;
    private String name;
    private int orderIndex;
    private UUID userId;

    public CalendarResponseDTO(UUID id, String name, int orderIndex, UUID userId) {
        this.id = id;
        this.name = name;
        this.orderIndex = orderIndex;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public UUID getUserId() {
        return userId;
    }
}
