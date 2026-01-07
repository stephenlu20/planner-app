package com.planner.api.dto;

import com.planner.api.entity.EntryType;
import com.planner.api.entity.EntrySubjectType;

import java.util.UUID;

public class EntryResponseDTO {

    private UUID id;

    private Long userId;

    private EntryType type;

    private EntrySubjectType subjectType;

    private UUID subjectId;

    private String label;

    private String value;

    private String note; // added note

    private Integer orderIndex; // include for frontend grouping / ordering

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

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

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}
