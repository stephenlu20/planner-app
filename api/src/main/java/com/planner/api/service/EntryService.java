package com.planner.api.service;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EntryService {

    private final EntryRepository recordRepository;

    public EntryService(EntryRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Entry createRecord(Entry record) {
        return recordRepository.save(record);
    }

    public Entry updateRecord(UUID recordId, Entry updatedRecord) {
        Entry existing = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        existing.setLabel(updatedRecord.getLabel());
        existing.setValue(updatedRecord.getValue());
        existing.setType(updatedRecord.getType());
        return recordRepository.save(existing);
    }

    public void deleteRecord(UUID recordId) {
        recordRepository.deleteById(recordId);
    }

    public Entry getRecord(UUID recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public List<Entry> getRecordsByUser(Long userId) {
        return recordRepository.findByUserId(userId);
    }

    public List<Entry> getRecordsBySubject(EntrySubjectType subjectType, UUID subjectId) {
        return recordRepository.findBySubjectTypeAndSubjectId(subjectType, subjectId);
    }
}
