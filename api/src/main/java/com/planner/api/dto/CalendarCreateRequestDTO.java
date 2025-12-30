package com.planner.api.dto;

import java.util.UUID;

public class CalendarCreateRequestDTO {

    private UUID userId;
    private String name;
    private int orderIndex;

    public CalendarCreateRequestDTO() { }

    public CalendarCreateRequestDTO(UUID userId, String name, int orderIndex) {
        this.userId = userId;
        this.name = name;
        this.orderIndex = orderIndex;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getOrderIndex() {
        return orderIndex;
    }
}
