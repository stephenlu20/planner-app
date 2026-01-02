package com.planner.api.dto;

public class CalendarCreateRequestDTO {

    private Long userId;
    private String name;
    private int orderIndex;

    public CalendarCreateRequestDTO() { }

    public CalendarCreateRequestDTO(Long userId, String name, int orderIndex) {
        this.userId = userId;
        this.name = name;
        this.orderIndex = orderIndex;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getOrderIndex() {
        return orderIndex;
    }
}
