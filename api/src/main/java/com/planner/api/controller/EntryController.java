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

    private final EntryService recordService;
    private final UserService userService;

    public EntryController(EntryService recordService, UserService userService) {
        this.recordService = recordService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}")
    public EntryResponseDTO createRecord(@PathVariable Long userId,
                                          @RequestBody EntryRequestDTO dto) {
        var user = userService.getUser(userId);
        Entry record = new Entry(user, dto.getType(), dto.getSubjectType(), dto.getSubjectId(),
                dto.getLabel(), dto.getValue());
        Entry saved = recordService.createRecord(record);
        return toResponseDTO(saved);
    }

    @GetMapping("/{recordId}")
    public EntryResponseDTO getRecord(@PathVariable UUID recordId) {
        return toResponseDTO(recordService.getRecord(recordId));
    }

    @GetMapping("/user/{userId}")
    public List<EntryResponseDTO> getRecordsByUser(@PathVariable Long userId) {
        return recordService.getRecordsByUser(userId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/subject/{subjectType}/{subjectId}")
    public List<EntryResponseDTO> getRecordsBySubject(@PathVariable String subjectType,
                                                       @PathVariable UUID subjectId) {
        return recordService.getRecordsBySubject(
                        Enum.valueOf(com.planner.api.entity.EntrySubjectType.class, subjectType.toUpperCase()),
                        subjectId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{recordId}")
    public EntryResponseDTO updateRecord(@PathVariable UUID recordId,
                                          @RequestBody EntryRequestDTO dto) {
        Entry updated = new Entry(null, dto.getType(), dto.getSubjectType(), dto.getSubjectId(),
                dto.getLabel(), dto.getValue());
        return toResponseDTO(recordService.updateRecord(recordId, updated));
    }

    @DeleteMapping("/{recordId}")
    public void deleteRecord(@PathVariable UUID recordId) {
        recordService.deleteRecord(recordId);
    }

    private EntryResponseDTO toResponseDTO(Entry record) {
        EntryResponseDTO dto = new EntryResponseDTO();
        dto.setId(record.getId());
        dto.setUserId(record.getUser().getId());
        dto.setType(record.getType());
        dto.setSubjectType(record.getSubjectType());
        dto.setSubjectId(record.getSubjectId());
        dto.setLabel(record.getLabel());
        dto.setValue(record.getValue());
        dto.setCreatedAt(record.getCreatedAt());
        return dto;
    }
}
