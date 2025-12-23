package com.planner.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDTO {
    private String title;
    private Instant date;
    private String noteText;
    private int position;

    private UUID parentEventId; // optional
}