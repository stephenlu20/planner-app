package com.planner.api.controller;

import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import com.planner.api.dto.EventCreateRequestDTO;
import com.planner.api.dto.EventReorderRequestDTO;
import com.planner.api.dto.EventResponseDTO;
import com.planner.api.service.EventService;
import com.planner.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://[::1]:5173"})
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public List<EventResponseDTO> getEventsForUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return eventService.getEventsForUser(user)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/calendar/{calendarId}")
    public List<EventResponseDTO> getEventsForCalendar(@PathVariable UUID calendarId) {
        return eventService.getEventsForCalendar(calendarId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PutMapping("/{eventId}/toggle")
    public EventResponseDTO toggleEventCompleted(@PathVariable UUID eventId) {
        return toDto(eventService.toggleCompleted(eventId));
    }

    @PutMapping("/reorder")
    public List<EventResponseDTO> reorderEvents(@RequestBody EventReorderRequestDTO request) {
        List<Event> updated = eventService.reorderEventsForCalendar(request.getCalendarId(), request.getOrderedEventIds());
        return updated.stream().map(this::toDto).toList();
    }

    @PostMapping
    public EventResponseDTO createEvent(@RequestBody EventCreateRequestDTO request) {
        Event event = eventService.createEvent(
                request.getNote(),
                request.getOrderIndex(),
                request.getUserId(),
                request.getCalendarId()
        );
        return toDto(event);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
    }

    private EventResponseDTO toDto(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),  // ADDED - now includes title
                event.getNote(),
                event.getDateTime(),
                event.getOrderIndex(),
                event.isCompleted(),
                event.getUser().getId(),
                event.getCalendar().getId(),
                event.getTemplateId()
        );
    }
}