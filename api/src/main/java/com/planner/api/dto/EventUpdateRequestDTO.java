package com.planner.api.dto;

public class EventUpdateRequestDTO {

    private String note;

    public EventUpdateRequestDTO() {
    }

    public EventUpdateRequestDTO(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}