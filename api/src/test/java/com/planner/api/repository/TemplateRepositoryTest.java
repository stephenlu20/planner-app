package com.planner.api.repository;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TemplateRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    private User user1;
    private User user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = new User("User1");
        user2 = new User("User2");

        userRepository.save(user1);
        userRepository.save(user2);

        Template t1 = new Template(null, "Template1", "Note1", user1, "Red", 30);
        Template t2 = new Template(null, "Template2", "Note2", user1, "Blue", 45);
        Template t3 = new Template(null, "Template3", "Note3", user2, "Green", 60);

        templateRepository.save(t1);
        templateRepository.save(t2);
        templateRepository.save(t3);
    }

    @Test
    void testFindAllByOwnerId() {
        List<Template> templatesUser1 = templateRepository.findAllByOwnerId(user1.getId());
        assertEquals(2, templatesUser1.size());
        assertTrue(templatesUser1.stream().allMatch(t -> t.getOwner().getId().equals(user1.getId())));

        List<Template> templatesUser2 = templateRepository.findAllByOwnerId(user2.getId());
        assertEquals(1, templatesUser2.size());
        assertEquals(user2.getId(), templatesUser2.get(0).getOwner().getId());
    }
}
