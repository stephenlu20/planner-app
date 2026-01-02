package com.planner.api.service;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.entity.EntryType;
import com.planner.api.entity.User;
import com.planner.api.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntryServiceTest {

    private EntryRepository recordRepository;
    private EntryService recordService;
    private User dummyUser;
    private UUID recordId;

    @BeforeEach
    void setUp() {
        recordRepository = mock(EntryRepository.class);
        recordService = new EntryService(recordRepository);

        dummyUser = new User("Test User");
        dummyUser.setId(1L);
        recordId = UUID.randomUUID();
    }

    @Test
    void testCreateRecord() {
        Entry record = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");
        when(recordRepository.save(record)).thenReturn(record);

        Entry saved = recordService.createRecord(record);

        assertEquals("Label", saved.getLabel());
        assertEquals("Value", saved.getValue());
        verify(recordRepository, times(1)).save(record);
    }

    @Test
    void testGetRecordExists() {
        Entry record = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");
        when(recordRepository.findById(recordId)).thenReturn(Optional.of(record));

        Entry found = recordService.getRecord(recordId);

        assertEquals("Label", found.getLabel());
        verify(recordRepository, times(1)).findById(recordId);
    }

    @Test
    void testGetRecordNotFound() {
        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> recordService.getRecord(recordId));
    }

    @Test
    void testUpdateRecord() {
        Entry existing = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Old", "OldValue");
        when(recordRepository.findById(recordId)).thenReturn(Optional.of(existing));
        when(recordRepository.save(any(Entry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Entry updated = new Entry(null, EntryType.NUMBER, EntrySubjectType.EVENT, UUID.randomUUID(), "New", "NewValue");

        Entry result = recordService.updateRecord(recordId, updated);

        assertEquals("New", result.getLabel());
        assertEquals("NewValue", result.getValue());
        assertEquals(EntryType.NUMBER, result.getType());
        verify(recordRepository, times(1)).findById(recordId);
        verify(recordRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateRecordNotFound() {
        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());
        Entry updated = new Entry(null, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");

        assertThrows(RuntimeException.class, () -> recordService.updateRecord(recordId, updated));
    }

    @Test
    void testDeleteRecord() {
        doNothing().when(recordRepository).deleteById(recordId);

        recordService.deleteRecord(recordId);

        verify(recordRepository, times(1)).deleteById(recordId);
    }

    @Test
    void testGetRecordsByUser() {
        Entry r1 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label1", "Value1");
        Entry r2 = new Entry(dummyUser, EntryType.NUMBER, EntrySubjectType.TEMPLATE, UUID.randomUUID(), "Label2", "Value2");

        when(recordRepository.findByUserId(dummyUser.getId())).thenReturn(List.of(r1, r2));

        List<Entry> records = recordService.getRecordsByUser(dummyUser.getId());

        assertEquals(2, records.size());
        verify(recordRepository, times(1)).findByUserId(dummyUser.getId());
    }

    @Test
    void testGetRecordsBySubject() {
        UUID subjectId = UUID.randomUUID();
        Entry r1 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, subjectId, "Label1", "Value1");

        when(recordRepository.findBySubjectTypeAndSubjectId(EntrySubjectType.EVENT, subjectId))
                .thenReturn(List.of(r1));

        List<Entry> records = recordService.getRecordsBySubject(EntrySubjectType.EVENT, subjectId);

        assertEquals(1, records.size());
        assertEquals("Label1", records.get(0).getLabel());
        verify(recordRepository, times(1)).findBySubjectTypeAndSubjectId(EntrySubjectType.EVENT, subjectId);
    }
}
