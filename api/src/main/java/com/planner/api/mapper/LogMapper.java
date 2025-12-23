package com.planner.api.mapper;

import com.planner.api.dto.log.*;
import com.planner.api.entity.Event;
import com.planner.api.entity.Log;
import com.planner.api.entity.Template;

public class LogMapper {

    private LogMapper() {}

    public static LogResponseDTO toResponse(Log log) {
        if (log == null) return null;

        return LogResponseDTO.builder()
                .id(log.getId())
                .type(log.getType())
                .label(log.getLabel())
                .value(log.getValue())
                .position(log.getPosition())
                .eventId(log.getEvent() != null ? log.getEvent().getId() : null)
                .templateId(log.getTemplate() != null ? log.getTemplate().getId() : null)
                .build();
    }

    public static LogSummaryDTO toSummary(Log log) {
        if (log == null) return null;

        return LogSummaryDTO.builder()
                .id(log.getId())
                .type(log.getType())
                .label(log.getLabel())
                .value(log.getValue())
                .build();
    }

    public static Log toEventLog(
            LogRequestDTO requestDTO,
            Event event
    ) {
        if (requestDTO == null) return null;

        return new Log(
                event,
                requestDTO.getType(),
                requestDTO.getLabel(),
                requestDTO.getValue(),
                requestDTO.getPosition()
        );
    }

    public static Log toTemplateLog(
            LogRequestDTO requestDTO,
            Template template
    ) {
        if (requestDTO == null) return null;

        return new Log(
                template,
                requestDTO.getType(),
                requestDTO.getLabel(),
                requestDTO.getValue(),
                requestDTO.getPosition()
        );
    }
}
