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

    private EntryRepository entryRepository;
    private EntryService entryService;
    private User dummyUser;
    private UUID entryId;

    @BeforeEach
    void setUp() {
        entryRepository = mock(EntryRepository.class);
        entryService = new EntryService(entryRepository);

        dummyUser = new User("Test User");
        dummyUser.setId(1L);
        entryId = UUID.randomUUID();
    }

    @Test
    void testCreateEntry() {
        Entry entry = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");
        when(entryRepository.save(entry)).thenReturn(entry);

        Entry saved = entryService.createEntry(entry);

        assertEquals("Label", saved.getLabel());
        assertEquals("Value", saved.getValue());
        verify(entryRepository, times(1)).save(entry);
    }

    @Test
    void testGetEntryExists() {
        Entry entry = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        Entry found = entryService.getEntry(entryId);

        assertEquals("Label", found.getLabel());
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testGetEntryNotFound() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> entryService.getEntry(entryId));
    }

    @Test
    void testUpdateEntry() {
        Entry existing = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Old", "OldValue");
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(existing));
        when(entryRepository.save(any(Entry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Entry updated = new Entry(null, EntryType.NUMBER, EntrySubjectType.EVENT, UUID.randomUUID(), "New", "NewValue");

        Entry result = entryService.updateEntry(entryId, updated);

        assertEquals("New", result.getLabel());
        assertEquals("NewValue", result.getValue());
        assertEquals(EntryType.NUMBER, result.getType());
        verify(entryRepository, times(1)).findById(entryId);
        verify(entryRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateEntryNotFound() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());
        Entry updated = new Entry(null, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Label", "Value");

        assertThrows(RuntimeException.class, () -> entryService.updateEntry(entryId, updated));
    }

    @Test
    void testDeleteEntry() {
        doNothing().when(entryRepository).deleteById(entryId);

        entryService.deleteEntry(entryId);

        verify(entryRepository, times(1)).deleteById(entryId);
    }
}
