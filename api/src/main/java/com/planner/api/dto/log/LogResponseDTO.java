package com.planner.api.dto.log;

import com.planner.api.entity.LogType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LogResponseDTO {

    private UUID id;
    private LogType type;
    private String label;
    private String value;
    private int position;

    private UUID eventId;
    private UUID templateId;
}
