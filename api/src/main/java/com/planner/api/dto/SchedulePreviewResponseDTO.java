package com.planner.api.dto;

import java.time.LocalDate;
import java.util.List;

public class SchedulePreviewResponseDTO {

    private List<LocalDate> dates;
    private int count;
    private LocalDate firstDate;
    private LocalDate lastDate;

    public SchedulePreviewResponseDTO() {
    }

    public SchedulePreviewResponseDTO(List<LocalDate> dates) {
        this.dates = dates;
        this.count = dates.size();
        this.firstDate = dates.isEmpty() ? null : dates.get(0);
        this.lastDate = dates.isEmpty() ? null : dates.get(dates.size() - 1);
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }
}