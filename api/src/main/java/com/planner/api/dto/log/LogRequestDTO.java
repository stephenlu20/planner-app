package com.planner.api.dto.log;

import com.planner.api.entity.LogType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogRequestDTO {

    private LogType type;
    private String label;
    private String value;
    private int index;
}
