package com.planner.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventResponseDTO {

    private UUID id;
    private String title;  // ADDED
    private String note;
    private LocalDateTime dateTime;
    private int orderIndex;
    private boolean completed;
    private Long userId;
    private UUID calendarId;
    private UUID templateId;

    public EventResponseDTO(UUID id, String title, String note, LocalDateTime dateTime, int orderIndex, boolean completed, Long userId, UUID calendarId) {
        this.id = id;
        this.title = title;  // ADDED
        this.note = note;
        this.dateTime = dateTime;
        this.orderIndex = orderIndex;
        this.completed = completed;
        this.userId = userId;
        this.calendarId = calendarId;
        this.templateId = null;
    }

    public EventResponseDTO(UUID id, String title, String note, LocalDateTime dateTime, int orderIndex, boolean completed, Long userId, UUID calendarId, UUID templateId) {
        this.id = id;
        this.title = title;  // ADDED
        this.note = note;
        this.dateTime = dateTime;
        this.orderIndex = orderIndex;
        this.completed = completed;
        this.userId = userId;
        this.calendarId = calendarId;
        this.templateId = templateId;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {  // ADDED
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

    public boolean isCompleted() {
        return completed;
    }

    public Long getUserId() {
        return userId;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public UUID getTemplateId() {
        return templateId;
    }
}