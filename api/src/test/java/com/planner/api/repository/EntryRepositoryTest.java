package com.planner.api.repository;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.entity.EntryType;
import com.planner.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = {"com.planner.api.entity"})
class EntryRepositoryTest {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserRepository userRepository;

    private User dummyUser;
    private UUID eventId;
    private UUID templateId;

    @BeforeEach
    void setUp() {
        dummyUser = userRepository.save(new User("Test User"));

        eventId = UUID.randomUUID();
        templateId = UUID.randomUUID();
    }

    @Test
    void testSaveAndFindById() {
        Entry entry = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, eventId, "Label", "Value");
        Entry saved = entryRepository.save(entry);

        Entry found = entryRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Label", found.getLabel());
        assertEquals("Value", found.getValue());
        assertEquals(dummyUser.getId(), found.getUser().getId());
    }

    @Test
    void testFindByUserId() {
        Entry r1 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, eventId, "Label1", "Value1");
        Entry r2 = new Entry(dummyUser, EntryType.NUMBER, EntrySubjectType.TEMPLATE, templateId, "Label2", "Value2");
        entryRepository.save(r1);
        entryRepository.save(r2);

        List<Entry> entries = entryRepository.findByUserId(dummyUser.getId());

        assertEquals(2, entries.size());
        assertTrue(entries.stream().anyMatch(r -> r.getLabel().equals("Label1")));
        assertTrue(entries.stream().anyMatch(r -> r.getLabel().equals("Label2")));
    }

    @Test
    void testFindBySubjectTypeAndSubjectId() {
        Entry r1 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, eventId, "Label1", "Value1");
        Entry r2 = new Entry(dummyUser, EntryType.NUMBER, EntrySubjectType.EVENT, eventId, "Label2", "Value2");
        Entry r3 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.TEMPLATE, templateId, "Label3", "Value3");

        entryRepository.save(r1);
        entryRepository.save(r2);
        entryRepository.save(r3);

        List<Entry> eventEntries = entryRepository.findBySubjectTypeAndSubjectId(EntrySubjectType.EVENT, eventId);

        assertEquals(2, eventEntries.size());
        assertTrue(eventEntries.stream().anyMatch(r -> r.getLabel().equals("Label1")));
        assertTrue(eventEntries.stream().anyMatch(r -> r.getLabel().equals("Label2")));

        List<Entry> templateEntries = entryRepository.findBySubjectTypeAndSubjectId(EntrySubjectType.TEMPLATE, templateId);

        assertEquals(1, templateEntries.size());
        assertEquals("Label3", templateEntries.get(0).getLabel());
    }

    @Test
    void testDeleteEntry() {
        Entry entry = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, eventId, "Label", "Value");
        Entry saved = entryRepository.save(entry);

        entryRepository.deleteById(saved.getId());

        assertFalse(entryRepository.findById(saved.getId()).isPresent());
    }
}
