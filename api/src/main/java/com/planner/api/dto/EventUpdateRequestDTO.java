package com.planner.api.dto;

import java.time.LocalDateTime;

public class EventUpdateRequestDTO {

    private String title;
    private String note;
    private LocalDateTime dateTime;

    public EventUpdateRequestDTO() {
    }

    public EventUpdateRequestDTO(String note) {
        this.note = note;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}