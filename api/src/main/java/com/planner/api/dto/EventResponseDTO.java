package com.planner.api.dto;

import java.util.UUID;

public class EventResponseDTO {

    private UUID id;
    private String note;
    private int orderIndex;
    private boolean completed;
    private Long userId;
    private UUID calendarId;

    public EventResponseDTO(UUID id, String note, int orderIndex, boolean completed, Long userId, UUID calendarId) {
        this.id = id;
        this.note = note;
        this.orderIndex = orderIndex;
        this.completed = completed;
        this.userId = userId;
        this.calendarId = calendarId;
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

    public Long getUserId() {
        return userId;
    }

    public UUID getCalendarId() {
        return calendarId;
    }
}
