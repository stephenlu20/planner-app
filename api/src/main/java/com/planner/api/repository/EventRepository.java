package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // Fetch all events for a calendar, ordered by position
    List<Event> findByCalendarOrderByPositionAsc(Calendar calendar);

    // Fetch events for a calendar on a specific date
    List<Event> findByCalendarAndDate(Calendar calendar, Instant date);

    // Fetch events for a calendar in a date range
    List<Event> findByCalendarAndDateBetween(Calendar calendar, Instant start, Instant end);

    // Fetch all top-level events (no parent)
    List<Event> findByCalendarAndParentEventIsNullOrderByPositionAsc(Calendar calendar);

    // Fetch all child events of a given parent event
    List<Event> findByParentEventOrderByPositionAsc(Event parentEvent);
}