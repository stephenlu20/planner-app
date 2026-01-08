package com.planner.api.service;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Template createTemplate(Template template, User owner) {
        template.setOwner(owner);
        return templateRepository.save(template);
    }

    public Template getTemplate(UUID id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public List<Template> getTemplatesByUser(Long userId) {
        return templateRepository.findAllByOwnerId(userId);
    }

    public Template updateTemplate(UUID id, Template updatedTemplate) {
        Template existing = getTemplate(id);
        existing.setName(updatedTemplate.getName());
        existing.setNote(updatedTemplate.getNote());
        return templateRepository.save(existing);
    }

    public void deleteTemplate(UUID id) {
        templateRepository.deleteById(id);
    }
}
