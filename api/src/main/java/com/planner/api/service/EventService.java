package com.planner.api.service;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import com.planner.api.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final CalendarService calendarService;

    public EventService(EventRepository eventRepository, UserService userService, CalendarService calendarService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.calendarService = calendarService;
    }

    public List<Event> getEventsForUser(User user) {
        return eventRepository.findByUserOrderByOrderIndexAsc(user);
    }

    public List<Event> getEventsForCalendar(UUID calendarId) {
        return eventRepository.findByCalendarIdOrderByOrderIndexAsc(calendarId);
    }

    public Event toggleCompleted(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.setCompleted(!event.isCompleted());
        return eventRepository.save(event);
    }

    public Event createEvent(String note, int orderIndex, Long userId, UUID calendarId) {
        User user = userService.getUser(userId);
        Calendar calendar = calendarService.getCalendar(calendarId);
        Event event = new Event(user, calendar, note, orderIndex);
        return eventRepository.save(event);
    }

    public List<Event> reorderEventsForUser(Long userId, List<UUID> orderedEventIds) {
        User user = userService.getUser(userId);
        List<Event> events = eventRepository.findByUserOrderByOrderIndexAsc(user);

        if (events.size() != orderedEventIds.size()) {
            throw new IllegalArgumentException("Mismatch in number of events");
        }

        Map<UUID, Event> eventMap = events.stream()
                .collect(Collectors.toMap(Event::getId, e -> e));

        for (int i = 0; i < orderedEventIds.size(); i++) {
            UUID id = orderedEventIds.get(i);
            Event e = eventMap.get(id);
            if (e == null) {
                throw new IllegalArgumentException("Invalid event ID: " + id);
            }
            e.setOrderIndex(i);
        }

        return eventRepository.saveAll(events);
    }

    public List<Event> reorderEventsForCalendar(UUID calendarId, List<UUID> orderedEventIds) {
        List<Event> events = eventRepository.findByCalendarIdOrderByOrderIndexAsc(calendarId);

        if (events.size() != orderedEventIds.size()) {
            throw new IllegalArgumentException("Mismatch in number of events");
        }

        Map<UUID, Event> eventMap = events.stream()
                .collect(Collectors.toMap(Event::getId, e -> e));

        for (int i = 0; i < orderedEventIds.size(); i++) {
            UUID id = orderedEventIds.get(i);
            Event e = eventMap.get(id);
            if (e == null) {
                throw new IllegalArgumentException("Invalid event ID: " + id);
            }
            e.setOrderIndex(i);
        }

        return eventRepository.saveAll(events);
    }

    public void deleteEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }

    public Event updateEvent(UUID eventId, String note) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        event.setNote(note);
        return eventRepository.save(event);
    }
}
