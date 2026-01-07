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

    @BeforeEach
    void setUp() {
        dummyUser = userRepository.save(new User("Test User"));

        eventId = UUID.randomUUID();
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
    void testDeleteEntry() {
        Entry entry = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, eventId, "Label", "Value");
        Entry saved = entryRepository.save(entry);

        entryRepository.deleteById(saved.getId());

        assertFalse(entryRepository.findById(saved.getId()).isPresent());
    }
}
