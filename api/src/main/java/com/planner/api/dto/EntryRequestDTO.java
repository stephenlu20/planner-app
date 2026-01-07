package com.planner.api.dto;

import com.planner.api.entity.EntryType;
import com.planner.api.entity.EntrySubjectType;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public class EntryRequestDTO {

    @NotNull
    private EntryType type;

    @NotNull
    private EntrySubjectType subjectType;

    @NotNull
    private UUID subjectId;

    @NotNull
    private String label;

    private String value;

    private String note; // added note

    // Getters & Setters
    public EntryType getType() { return type; }
    public void setType(EntryType type) { this.type = type; }

    public EntrySubjectType getSubjectType() { return subjectType; }
    public void setSubjectType(EntrySubjectType subjectType) { this.subjectType = subjectType; }

    public UUID getSubjectId() { return subjectId; }
    public void setSubjectId(UUID subjectId) { this.subjectId = subjectId; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
