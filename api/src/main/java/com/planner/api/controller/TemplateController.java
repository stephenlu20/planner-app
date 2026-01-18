package com.planner.api.controller;

import com.planner.api.dto.*;
import com.planner.api.entity.Event;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.service.TemplateService;
import com.planner.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/templates")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://[::1]:5173"})
public class TemplateController {

    private final TemplateService templateService;
    private final UserService userService;

    public TemplateController(TemplateService templateService, UserService userService) {
        this.templateService = templateService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}")
    public TemplateResponseDTO createTemplate(@PathVariable Long userId,
                                              @RequestBody TemplateRequestDTO dto) {
        User owner = userService.getUser(userId);
        Template template = new Template();
        template.setName(dto.getName());
        template.setNote(dto.getNote());

        Template saved = templateService.createTemplate(template, owner, null);
        return mapToDTO(saved);
    }

    @GetMapping("/{id}")
    public TemplateResponseDTO getTemplate(@PathVariable UUID id) {
        return mapToDTO(templateService.getTemplate(id));
    }

    @GetMapping("/user/{userId}")
    public List<TemplateResponseDTO> getTemplatesByUser(@PathVariable Long userId) {
        return templateService.getTemplatesByUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public TemplateResponseDTO updateTemplate(@PathVariable UUID id,
                                              @RequestBody TemplateRequestDTO dto) {
        Template template = new Template();
        template.setName(dto.getName());
        template.setNote(dto.getNote());

        Template updated = templateService.updateTemplate(id, template, null);
        return mapToDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
    }

    /**
     * Populate calendar with events from template's schedule rule
     */
    @PostMapping("/populate")
    public List<EventResponseDTO> populateCalendar(@RequestBody TemplatePopulateRequestDTO request) {
        List<Event> events = templateService.populateCalendarFromTemplate(
            request.getTemplateId(),
            request.getCalendarId(),
            request.getDeleteStrategy()
        );
        
        return events.stream()
            .map(this::mapEventToDTO)
            .collect(Collectors.toList());
    }

    private TemplateResponseDTO mapToDTO(Template template) {
        TemplateResponseDTO dto = new TemplateResponseDTO();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setNote(template.getNote());
        dto.setOwnerId(template.getOwner() != null ? template.getOwner().getId() : null);
        return dto;
    }

    private EventResponseDTO mapEventToDTO(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getNote(),
            event.getOrderIndex(),
            event.isCompleted(),
            event.getUser().getId(),
            event.getCalendar().getId(),
            event.getTemplateId()
        );
    }
}