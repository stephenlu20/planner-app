package com.planner.api.controller;

import com.planner.api.dto.EntryRequestDTO;
import com.planner.api.dto.EntryResponseDTO;
import com.planner.api.dto.EntryUpdateDTO;
import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.service.EntryService;
import com.planner.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entries")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://[::1]:5173"})
public class EntryController {

    private final EntryService entryService;
    private final UserService userService;

    public EntryController(EntryService entryService, UserService userService) {
        this.entryService = entryService;
        this.userService = userService;
    }

    // CREATE
    @PostMapping("/user/{userId}")
    public EntryResponseDTO createEntry(@PathVariable Long userId,
                                        @Valid @RequestBody EntryRequestDTO dto) {
        // Validate user
        var user = userService.getUser(userId);

        // Only create entry; structural fields come from DTO
        Entry entry = new Entry(user, dto.getType(), dto.getSubjectType(),
                                dto.getSubjectId(), dto.getLabel(),
                                dto.getValue());
        entry.setNote(dto.getNote()); // Include note
        Entry saved = entryService.createEntry(entry);
        return toResponseDTO(saved);
    }

    // READ SINGLE
    @GetMapping("/{entryId}")
    public EntryResponseDTO getEntry(@PathVariable UUID entryId) {
        return toResponseDTO(entryService.getEntry(entryId));
    }

    // REMOVE getEntrysByUser endpoint: no service method exists
    // If needed, implement via subject-based queries or journal aggregation

    // READ BY SUBJECT
    @GetMapping("/subject/{subjectType}/{subjectId}")
    public List<EntryResponseDTO> getEntriesBySubject(@PathVariable String subjectType,
                                                      @PathVariable UUID subjectId) {
        EntrySubjectType typeEnum;
        try {
            typeEnum = EntrySubjectType.valueOf(subjectType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid subjectType: " + subjectType);
        }

        return entryService.getEntriesBySubject(typeEnum, subjectId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{entryId}")
    public EntryResponseDTO updateEntry(@PathVariable UUID entryId,
                                        @Valid @RequestBody EntryUpdateDTO dto) {
        // Fetch the existing entry
        Entry existing = entryService.getEntry(entryId);

        // Only update editable fields
        existing.setLabel(dto.getLabel());
        existing.setValue(dto.getValue());
        existing.setNote(dto.getNote());
        if (dto.getOrderIndex() != null) {
            existing.setOrderIndex(dto.getOrderIndex());
        }

        // Save update
        Entry updated = entryService.updateEntry(entryId, existing);

        return toResponseDTO(updated);
    }
    
    // DELETE
    @DeleteMapping("/{entryId}")
    public void deleteEntry(@PathVariable UUID entryId) {
        entryService.deleteEntry(entryId);
    }

    // MAPPER
    private EntryResponseDTO toResponseDTO(Entry entry) {
        EntryResponseDTO dto = new EntryResponseDTO();
        dto.setId(entry.getId());
        dto.setUserId(entry.getUser().getId());
        dto.setType(entry.getType());
        dto.setSubjectType(entry.getSubjectType());
        dto.setSubjectId(entry.getSubjectId());
        dto.setLabel(entry.getLabel());
        dto.setValue(entry.getValue());
        dto.setNote(entry.getNote()); // Include note in response
        return dto;
    }
}
