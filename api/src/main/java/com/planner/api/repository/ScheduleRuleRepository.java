package com.planner.api.repository;

import com.planner.api.entity.ScheduleRule;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleRuleRepository extends JpaRepository<ScheduleRule, UUID> {

    List<ScheduleRule> findByUser(User user);

    List<ScheduleRule> findByTemplate(Template template);

    List<ScheduleRule> findByUserAndActiveTrue(User user);
}
