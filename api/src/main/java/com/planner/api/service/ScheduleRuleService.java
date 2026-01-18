package com.planner.api.service;

import com.planner.api.entity.ScheduleRule;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.repository.ScheduleRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleRuleService {

    private final ScheduleRuleRepository scheduleRuleRepository;

    public ScheduleRuleService(ScheduleRuleRepository scheduleRuleRepository) {
        this.scheduleRuleRepository = scheduleRuleRepository;
    }

    public ScheduleRule create(ScheduleRule rule) {
        return scheduleRuleRepository.save(rule);
    }

    public ScheduleRule update(ScheduleRule rule) {
        return scheduleRuleRepository.save(rule);
    }

    public ScheduleRule getById(UUID id) {
        return scheduleRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ScheduleRule not found"));
    }

    public List<ScheduleRule> getByUser(User user) {
        return scheduleRuleRepository.findByUser(user);
    }

    public List<ScheduleRule> getActiveByUser(User user) {
        return scheduleRuleRepository.findByUserAndActiveTrue(user);
    }

    public List<ScheduleRule> getByTemplate(Template template) {
        return scheduleRuleRepository.findByTemplate(template);
    }

    public void delete(UUID id) {
        scheduleRuleRepository.deleteById(id);
    }
}
