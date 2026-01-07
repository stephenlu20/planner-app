package com.planner.api.controller;

import com.planner.api.dto.EntryRequestDTO;
import com.planner.api.dto.EntryResponseDTO;
import com.planner.api.entity.Entry;
import com.planner.api.service.EntryService;
import com.planner.api.service.UserService;
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

    @PostMapping("/user/{userId}")
    public EntryResponseDTO createEntry(@PathVariable Long userId,
                                          @RequestBody EntryRequestDTO dto) {
        var user = userService.getUser(userId);
        Entry entry = new Entry(user, dto.getType(), dto.getSubjectType(), dto.getSubjectId(),
                dto.getLabel(), dto.getValue());
        Entry saved = entryService.createEntry(entry);
        return toResponseDTO(saved);
    }

    @GetMapping("/{entryId}")
    public EntryResponseDTO getEntry(@PathVariable UUID entryId) {
        return toResponseDTO(entryService.getEntry(entryId));
    }

    @GetMapping("/user/{userId}")
    public List<EntryResponseDTO> getEntrysByUser(@PathVariable Long userId) {
        return entryService.getEntrysByUser(userId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/subject/{subjectType}/{subjectId}")
    public List<EntryResponseDTO> getEntrysBySubject(@PathVariable String subjectType,
                                                       @PathVariable UUID subjectId) {
        return entryService.getEntrysBySubject(
                        Enum.valueOf(com.planner.api.entity.EntrySubjectType.class, subjectType.toUpperCase()),
                        subjectId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{entryId}")
    public EntryResponseDTO updateEntry(@PathVariable UUID entryId,
                                          @RequestBody EntryRequestDTO dto) {
        Entry updated = new Entry(null, dto.getType(), dto.getSubjectType(), dto.getSubjectId(),
                dto.getLabel(), dto.getValue());
        return toResponseDTO(entryService.updateEntry(entryId, updated));
    }

    @DeleteMapping("/{entryId}")
    public void deleteEntry(@PathVariable UUID entryId) {
        entryService.deleteEntry(entryId);
    }

    private EntryResponseDTO toResponseDTO(Entry entry) {
        EntryResponseDTO dto = new EntryResponseDTO();
        dto.setId(entry.getId());
        dto.setUserId(entry.getUser().getId());
        dto.setType(entry.getType());
        dto.setSubjectType(entry.getSubjectType());
        dto.setSubjectId(entry.getSubjectId());
        dto.setLabel(entry.getLabel());
        dto.setValue(entry.getValue());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
}
