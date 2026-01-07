package com.planner.api.dto;

import jakarta.validation.constraints.NotNull;

public class EntryUpdateDTO {

    @NotNull
    private String label;

    private String value;

    private String note;

    // Optional: orderIndex if you want to allow reordering
    private Integer orderIndex;

    // Getters & Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}
