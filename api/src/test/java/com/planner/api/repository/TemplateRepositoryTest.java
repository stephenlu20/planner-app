package com.planner.api.repository;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class TemplateRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Template topLevelTemplate;

    @BeforeEach
    void setup() {
        // 1️⃣ Persist the user first (managed entity)
        testUser = User.builder()
                .name("Test User")
                .build();
        testUser = userRepository.saveAndFlush(testUser); // managed User

        // 2️⃣ Create top-level template
        topLevelTemplate = Template.builder()
                .user(testUser)
                .title("Top Template")
                .position(0)
                .build();
        topLevelTemplate = templateRepository.saveAndFlush(topLevelTemplate);

        // 3️⃣ Add child templates
        Template child1 = Template.builder()
                .user(testUser)
                .parentTemplate(topLevelTemplate)
                .title("Child 1")
                .position(0)
                .build();
        Template child2 = Template.builder()
                .user(testUser)
                .parentTemplate(topLevelTemplate)
                .title("Child 2")
                .position(1)
                .build();

        templateRepository.save(child1);
        templateRepository.save(child2);
    }

    @Test
    void shouldSaveAndFindTemplateById() {
        Template found = templateRepository.findById(topLevelTemplate.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Top Template");
    }

    @Test
    void shouldFindTopLevelTemplatesByUser() {
        List<Template> topLevel = templateRepository.findByUserAndParentTemplateIsNullOrderByPositionAsc(testUser);
        assertThat(topLevel).hasSize(1);
        assertThat(topLevel.get(0).getTitle()).isEqualTo("Top Template");
    }

    @Test
    void shouldFindChildTemplatesByParent() {
        List<Template> children = templateRepository.findByParentTemplateOrderByPositionAsc(topLevelTemplate);
        assertThat(children).hasSize(2);
        assertThat(children.get(0).getTitle()).isEqualTo("Child 1");
        assertThat(children.get(1).getTitle()).isEqualTo("Child 2");
    }

    @Test
    void shouldCheckExistenceByTitleForUser() {
        boolean exists = templateRepository.existsByUserAndParentTemplateIsNullAndTitle(testUser, "Top Template");
        assertThat(exists).isTrue();

        boolean notExists = templateRepository.existsByUserAndParentTemplateIsNullAndTitle(testUser, "Nonexistent");
        assertThat(notExists).isFalse();
    }
}
