package com.planner.api.dto;

import com.planner.api.entity.EntryType;
import com.planner.api.entity.EntrySubjectType;

import java.time.Instant;
import java.util.UUID;

public class EntryResponseDTO {

    private UUID id;
    private UUID userId;
    private EntryType type;
    private EntrySubjectType subjectType;
    private UUID subjectId;
    private String label;
    private String value;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public EntrySubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(EntrySubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
