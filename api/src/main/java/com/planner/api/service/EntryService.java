package com.planner.api.service;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Transactional
    public Entry createEntry(Entry entry) {
        List<Entry> existing = entryRepository
        .findBySubjectTypeAndSubjectIdOrderByOrderIndexAsc(
            entry.getSubjectType(),
            entry.getSubjectId()
        );
        
        int nextIndex = existing.isEmpty() ? 0 : existing.get(existing.size() - 1).getOrderIndex() + 1;
        entry.setOrderIndex(nextIndex);

        return entryRepository.save(entry);
    }

    @Transactional
    public Entry updateEntry(UUID entryId, Entry updatedEntry) {
        Entry existing = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        existing.setLabel(updatedEntry.getLabel());
        existing.setValue(updatedEntry.getValue());
        existing.setNote(updatedEntry.getNote());
        return entryRepository.save(existing);
    }

    @Transactional
    public void deleteEntry(UUID entryId) {
        entryRepository.deleteById(entryId);
    }

    public Entry getEntry(UUID entryId) {
        return entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    public List<Entry> getEntriesBySubject(EntrySubjectType subjectType, UUID subjectId) {
        return entryRepository
            .findBySubjectTypeAndSubjectIdOrderByOrderIndexAsc(subjectType, subjectId);
    }
}
