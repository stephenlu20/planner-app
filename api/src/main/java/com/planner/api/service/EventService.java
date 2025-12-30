package com.planner.api.service;

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

    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public List<Event> getEventsForUser(User user) {
        return eventRepository.findByUserOrderByOrderIndexAsc(user);
    }

    public Event toggleCompleted(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.setCompleted(!event.isCompleted());
        return event;
    }

    public Event createEvent(String note, int orderIndex, UUID userId) {
        User user = userService.getUser(userId);
        Event event = new Event(user, note, orderIndex);
        return eventRepository.save(event);
    }

    public List<Event> reorderEvents(UUID userId, List<UUID> orderedEventIds) {
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

    public void deleteEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }
}
