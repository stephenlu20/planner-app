package com.planner.api.dto;

import com.planner.api.entity.EventDeleteStrategy;
import java.util.UUID;

public class TemplatePopulateRequestDTO {

    private UUID templateId;
    private UUID calendarId;
    private EventDeleteStrategy deleteStrategy;

    public TemplatePopulateRequestDTO() {
    }

    public TemplatePopulateRequestDTO(UUID templateId, UUID calendarId, EventDeleteStrategy deleteStrategy) {
        this.templateId = templateId;
        this.calendarId = calendarId;
        this.deleteStrategy = deleteStrategy;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(UUID calendarId) {
        this.calendarId = calendarId;
    }

    public EventDeleteStrategy getDeleteStrategy() {
        return deleteStrategy;
    }

    public void setDeleteStrategy(EventDeleteStrategy deleteStrategy) {
        this.deleteStrategy = deleteStrategy;
    }
}