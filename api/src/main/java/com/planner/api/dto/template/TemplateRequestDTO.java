package com.planner.api.dto.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateRequestDTO {
    private String title;
    private String description;
    private int position;

    private UUID parentTemplateId; // optional
}