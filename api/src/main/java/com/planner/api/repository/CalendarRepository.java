package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, UUID> {

    // Fetch all calendars for a given user, ordered by position
    List<Calendar> findByUserOrderByPositionAsc(User user);
}