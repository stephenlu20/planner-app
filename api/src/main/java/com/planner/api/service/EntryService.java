package com.planner.api.service;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Entry createEntry(Entry entry) {
        return entryRepository.save(entry);
    }

    public Entry updateEntry(UUID entryId, Entry updatedEntry) {
        Entry existing = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        existing.setLabel(updatedEntry.getLabel());
        existing.setValue(updatedEntry.getValue());
        existing.setType(updatedEntry.getType());
        return entryRepository.save(existing);
    }

    public void deleteEntry(UUID entryId) {
        entryRepository.deleteById(entryId);
    }

    public Entry getEntry(UUID entryId) {
        return entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    public List<Entry> getEntrysByUser(Long userId) {
        return entryRepository.findByUserId(userId);
    }

    public List<Entry> getEntrysBySubject(EntrySubjectType subjectType, UUID subjectId) {
        return entryRepository.findBySubjectTypeAndSubjectId(subjectType, subjectId);
    }
}
