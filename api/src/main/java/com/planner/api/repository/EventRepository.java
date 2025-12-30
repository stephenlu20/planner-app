package com.planner.api.repository;

import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findByUserOrderByOrderIndexAsc(User user);

    List<Event> findByUserAndCompleted(User user, boolean completed);
}
