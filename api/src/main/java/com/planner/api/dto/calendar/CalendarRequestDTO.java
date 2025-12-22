package com.planner.api.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarRequestDTO {
    private String name;
    private String color;
    private int index; // optional; could default to end of list
}
