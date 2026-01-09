package com.planner.api.repository;

import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    /* Existing queries */

    List<Event> findByUserOrderByOrderIndexAsc(User user);

    List<Event> findByUserAndCompleted(User user, boolean completed);

    List<Event> findByCalendarIdOrderByOrderIndexAsc(UUID calendarId);

    List<Event> findByCalendarIdAndCompleted(UUID calendarId, boolean completed);

    List<Event> findByTemplateId(UUID templateId);

    List<Event> findByTemplateIdAndDateTimeBetween(
            UUID templateId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Event> findByTemplateIdAndCalendarIdAndDateTimeBetween(
            UUID templateId,
            UUID calendarId,
            LocalDateTime start,
            LocalDateTime end
    );

    void deleteByTemplateIdAndCalendarIdAndDateTimeBetween(
            UUID templateId,
            UUID calendarId,
            LocalDateTime start,
            LocalDateTime end
    );
}
