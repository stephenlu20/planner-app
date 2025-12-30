package com.planner.api.dto;

import java.util.UUID;

public class EventCreateRequestDTO {

    private String note;
    private int orderIndex;
    private UUID userId;

    public EventCreateRequestDTO() {
        // default for JSON deserialization
    }

    public EventCreateRequestDTO(String note, int orderIndex, UUID userId) {
        this.note = note;
        this.orderIndex = orderIndex;
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public UUID getUserId() {
        return userId;
    }
}
