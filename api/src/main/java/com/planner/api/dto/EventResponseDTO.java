package com.planner.api.dto;

import java.util.UUID;

public class EventResponseDTO {

    private UUID id;
    private String note;
    private int orderIndex;
    private boolean completed;
    private UUID userId;

    public EventResponseDTO(UUID id, String note, int orderIndex, boolean completed, UUID userId) {
        this.id = id;
        this.note = note;
        this.orderIndex = orderIndex;
        this.completed = completed;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isCompleted() {
        return completed;
    }

    public UUID getUserId() {
        return userId;
    }
}
