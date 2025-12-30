package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CalendarRepository extends JpaRepository<Calendar, UUID> {

    List<Calendar> findByUserOrderByOrderIndexAsc(User user);
}
