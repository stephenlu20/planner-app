package com.planner.api.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResponseDTO {
    private UUID id;
    private String name;
    private String color;
    private int index;
    private List<EventSummaryDTO> events;
}