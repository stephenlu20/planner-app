package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // Fetch all events for a calendar, ordered by index
    List<Event> findByCalendarOrderByIndexAsc(Calendar calendar);

    // Fetch events for a calendar on a specific date
    List<Event> findByCalendarAndDate(Calendar calendar, LocalDate date);

    // Fetch events for a calendar in a date range
    List<Event> findByCalendarAndDateBetween(Calendar calendar, LocalDate start, LocalDate end);

    // Fetch all top-level events (no parent)
    List<Event> findByCalendarAndParentEventIsNullOrderByIndexAsc(Calendar calendar);

    // Fetch all child events of a given parent event
    List<Event> findByParentEventOrderByIndexAsc(Event parentEvent);
}