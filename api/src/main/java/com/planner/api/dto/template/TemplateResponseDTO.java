package com.planner.api.dto.template;

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
public class TemplateResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private int index;

    private UUID parentTemplateId; // optional

    private List<TemplateSummaryDTO> childTemplates; // nested children
    private List<LogSummaryDTO> logs; // logs associated with this template
}