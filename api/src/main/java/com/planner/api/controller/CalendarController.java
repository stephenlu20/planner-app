package com.planner.api.controller;

import com.planner.api.dto.CalendarCreateRequestDTO;
import com.planner.api.dto.CalendarReorderRequestDTO;
import com.planner.api.dto.CalendarResponseDTO;
import com.planner.api.entity.Calendar;
import com.planner.api.service.CalendarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/user/{userId}")
    public List<CalendarResponseDTO> getCalendarsForUser(@PathVariable UUID userId) {
        return calendarService.getCalendarsForUser(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CalendarResponseDTO createCalendar(@RequestBody CalendarCreateRequestDTO request) {
        Calendar calendar = calendarService.createCalendar(request.getUserId(), request.getName(), request.getOrderIndex());
        return toDto(calendar);
    }

    @PutMapping("/reorder")
    public List<CalendarResponseDTO> reorderCalendars(@RequestBody CalendarReorderRequestDTO request) {
        return calendarService.reorderCalendars(request.getUserId(), request.getOrderedCalendarIds())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{calendarId}")
    public void deleteCalendar(@PathVariable UUID calendarId) {
        calendarService.deleteCalendar(calendarId);
    }

    private CalendarResponseDTO toDto(Calendar calendar) {
        return new CalendarResponseDTO(
                calendar.getId(),
                calendar.getName(),
                calendar.getOrderIndex(),
                calendar.getUser().getId()
        );
    }
}
