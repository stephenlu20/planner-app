package com.planner.api.mapper;

import com.planner.api.dto.calendar.CalendarRequestDTO;
import com.planner.api.dto.calendar.CalendarResponseDTO;
import com.planner.api.dto.calendar.CalendarSummaryDTO;
import com.planner.api.dto.event.EventSummaryDTO;
import com.planner.api.entity.Calendar;

import java.util.stream.Collectors;

public class CalendarMapper {

    public static CalendarResponseDTO toResponseDTO(Calendar calendar) {
        if (calendar == null) return null;

        return CalendarResponseDTO.builder()
                .id(calendar.getId())
                .name(calendar.getName())
                .color(calendar.getColor())
                .index(calendar.getIndex())
                .events(calendar.getEvents().stream()
                        .map(event -> EventSummaryDTO.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .index(event.getIndex())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static CalendarSummaryDTO toSummaryDTO(Calendar calendar) {
        if (calendar == null) return null;

        return CalendarSummaryDTO.builder()
                .id(calendar.getId())
                .name(calendar.getName())
                .color(calendar.getColor())
                .index(calendar.getIndex())
                .build();
    }

    public static Calendar toEntity(CalendarRequestDTO requestDTO, com.planner.api.entity.User user) {
        if (requestDTO == null) return null;

        return new Calendar(
                user,
                requestDTO.getName(),
                requestDTO.getColor(),
                requestDTO.getIndex()
        );
    }

    public static void updateEntity(Calendar calendar, CalendarRequestDTO requestDTO) {
        if (calendar == null || requestDTO == null) return;

        calendar.setName(requestDTO.getName());
        calendar.setColor(requestDTO.getColor());
        calendar.reorder(requestDTO.getIndex());
    }
}