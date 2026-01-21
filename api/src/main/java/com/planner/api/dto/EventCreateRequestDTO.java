package com.planner.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventCreateRequestDTO {

    private String title;
    private String note;
    private LocalDateTime dateTime; 
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

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
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
