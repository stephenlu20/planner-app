package com.planner.api.mapper;

import com.planner.api.dto.event.*;
import com.planner.api.dto.log.LogSummaryDTO;
import com.planner.api.entity.Event;

import java.util.stream.Collectors;

public class EventMapper {

    public static EventSummaryDTO toSummaryDTO(Event event) {
        if (event == null) return null;

        return EventSummaryDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .date(event.getDate())
                .isCompleted(event.isCompleted())
                .index(event.getIndex())
                .build();
    }

    public static EventResponseDTO toResponseDTO(Event event) {
        if (event == null) return null;

        return EventResponseDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .date(event.getDate())
                .isCompleted(event.isCompleted())
                .noteText(event.getNoteText())
                .index(event.getIndex())
                .calendarId(event.getCalendar().getId())
                .parentEventId(
                        event.getParentEvent() != null
                                ? event.getParentEvent().getId()
                                : null
                )
                .subTasks(event.getSubTasks().stream()
                        .map(EventMapper::toSummaryDTO)
                        .collect(Collectors.toList()))
                .logs(event.getLogs().stream()
                        .map(log -> LogSummaryDTO.builder()
                                .id(log.getId())
                                .label(log.getLabel())
                                .type(log.getType())
                                .index(log.getIndex())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Event toEntity(
        EventRequestDTO requestDTO,
        com.planner.api.entity.Calendar calendar,
        Event parentEvent
    ) {
        if (requestDTO == null) return null;

        Event event;

        if (parentEvent != null) {
            event = new Event(
                    calendar,
                    parentEvent,
                    requestDTO.getTitle(),
                    requestDTO.getDate(),
                    requestDTO.getIndex()
            );
        } else {
            event = new Event(
                    calendar,
                    requestDTO.getTitle(),
                    requestDTO.getDate(),
                    requestDTO.getIndex()
            );
        }

        event.updateNote(requestDTO.getNoteText());

        return event;
    }
    public static void updateEntity(Event event, EventRequestDTO requestDTO) {
        if (event == null || requestDTO == null) return;

        event.updateTitle(requestDTO.getTitle());
        event.updateDate(requestDTO.getDate());
        event.updateNote(requestDTO.getNoteText());
        event.reorder(requestDTO.getIndex());
    }
}
