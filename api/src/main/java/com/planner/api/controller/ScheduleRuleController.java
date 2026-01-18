package com.planner.api.controller;

import com.planner.api.dto.ScheduleRuleRequestDTO;
import com.planner.api.dto.ScheduleRuleResponseDTO;
import com.planner.api.entity.ScheduleRule;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.mapper.ScheduleRuleMapper;
import com.planner.api.service.ScheduleRuleService;
import com.planner.api.service.TemplateService;
import com.planner.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule-rules")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ScheduleRuleController {

    private final ScheduleRuleService scheduleRuleService;
    private final UserService userService;
    private final TemplateService templateService;

    public ScheduleRuleController(ScheduleRuleService scheduleRuleService,
                                  UserService userService,
                                  TemplateService templateService) {
        this.scheduleRuleService = scheduleRuleService;
        this.userService = userService;
        this.templateService = templateService;
    }

    @PostMapping
    public ScheduleRuleResponseDTO createRule(@RequestBody ScheduleRuleRequestDTO dto) {
        User user = userService.getUser(dto.getUserId());
        Template template = templateService.getTemplate(dto.getTemplateId());

        ScheduleRule rule = ScheduleRuleMapper.toEntity(dto, user, template);
        ScheduleRule saved = scheduleRuleService.create(rule);

        return ScheduleRuleMapper.toDTO(saved);
    }

    @GetMapping("/template/{templateId}")
    public List<ScheduleRuleResponseDTO> getRulesByTemplate(@PathVariable UUID templateId) {
        Template template = templateService.getTemplate(templateId);
        return scheduleRuleService.getByTemplate(template)
                .stream()
                .map(ScheduleRuleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<ScheduleRuleResponseDTO> getRulesByUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return scheduleRuleService.getByUser(user)
                .stream()
                .map(ScheduleRuleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{ruleId}")
    public ScheduleRuleResponseDTO updateRule(@PathVariable UUID ruleId,
                                            @RequestBody ScheduleRuleRequestDTO dto) {
        ScheduleRule existing = scheduleRuleService.getById(ruleId);

        // Fetch related entities
        User user = userService.getUser(dto.getUserId());
        Template template = templateService.getTemplate(dto.getTemplateId());

        // Update fields individually
        existing.setUser(user);
        existing.setTemplate(template);
        existing.setFrequency(dto.getFrequency());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setDaysOfWeek(dto.getDaysOfWeek());
        existing.setMonthlyPatternType(dto.getMonthlyPatternType());
        existing.setDayOfMonth(dto.getDayOfMonth());
        existing.setWeekOrdinal(dto.getWeekOrdinal());
        existing.setWeekday(dto.getWeekday());
        existing.setActive(dto.isActive());

        ScheduleRule saved = scheduleRuleService.update(existing);
        return ScheduleRuleMapper.toDTO(saved);
    }

    @DeleteMapping("/{ruleId}")
    public void deleteRule(@PathVariable UUID ruleId) {
        scheduleRuleService.delete(ruleId);
    }
}
