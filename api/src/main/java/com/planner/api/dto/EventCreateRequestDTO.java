package com.planner.api.dto;

import java.util.UUID;

public class EventCreateRequestDTO {

    private String note;
    private int orderIndex;
    private Long userId;
    private UUID calendarId;

    public EventCreateRequestDTO() {
    }

    public EventCreateRequestDTO(String note, int orderIndex, Long userId, UUID calendarId) {
        this.note = note;
        this.orderIndex = orderIndex;
        this.userId = userId;
        this.calendarId = calendarId;
    }

    public String getNote() {
        return note;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public Long getUserId() {
        return userId;
    }

    public UUID getCalendarId() {
        return calendarId;
    }
}
