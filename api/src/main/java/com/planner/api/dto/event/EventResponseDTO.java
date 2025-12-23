package com.planner.api.dto.event;

import com.planner.api.dto.log.LogSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {
    private UUID id;
    private String title;
    private LocalDate date;
    private boolean isCompleted;
    private String noteText;
    private int position;

    private UUID calendarId;
    private UUID parentEventId;

    private List<EventSummaryDTO> subTasks;
    private List<LogSummaryDTO> logs;
}