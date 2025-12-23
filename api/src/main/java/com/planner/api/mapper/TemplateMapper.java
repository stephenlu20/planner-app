package com.planner.api.mapper;

import com.planner.api.dto.log.LogSummaryDTO;
import com.planner.api.entity.Template;
import com.planner.api.dto.template.*;

import java.util.stream.Collectors;

public class TemplateMapper {

    public static TemplateResponseDTO toResponseDTO(Template template) {
        if (template == null) return null;

        return TemplateResponseDTO.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .position(template.getPosition())
                .parentTemplateId(template.getParentTemplate() != null ? template.getParentTemplate().getId() : null)
                .childTemplates(template.getChildTemplates().stream()
                        .map(t -> TemplateSummaryDTO.builder()
                                .id(t.getId())
                                .title(t.getTitle())
                                .position(t.getPosition())
                                .build())
                        .collect(Collectors.toList()))
                .logs(template.getLogs().stream()
                        .map(log -> LogSummaryDTO.builder()
                                .id(log.getId())
                                .label(log.getLabel())
                                .type(log.getType())
                                .position(log.getPosition())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static TemplateSummaryDTO toSummaryDTO(Template template) {
        if (template == null) return null;

        return TemplateSummaryDTO.builder()
                .id(template.getId())
                .title(template.getTitle())
                .position(template.getPosition())
                .build();
    }

    public static Template toEntity(TemplateRequestDTO requestDTO, com.planner.api.entity.User user, Template parentTemplate) {
        if (requestDTO == null) return null;

        Template template = new Template(user, parentTemplate, requestDTO.getTitle(), requestDTO.getPosition());
        template.updateDescription(requestDTO.getDescription());
        template.setUser(user);
        template.setTitle(requestDTO.getTitle());
        template.setDescription(requestDTO.getDescription());
        template.setPosition(requestDTO.getPosition());
        template.setParentTemplate(parentTemplate);

        return template;
    }

    public static void updateEntity(Template template, TemplateRequestDTO requestDTO, Template parentTemplate) {
        if (template == null || requestDTO == null) return;

        template.setTitle(requestDTO.getTitle());
        template.setDescription(requestDTO.getDescription());
        template.setPosition(requestDTO.getPosition());
        template.setParentTemplate(parentTemplate);
    }
}