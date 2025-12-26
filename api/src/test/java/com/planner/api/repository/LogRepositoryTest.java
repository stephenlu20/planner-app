package com.planner.api.repository;

import com.planner.api.entity.AuditFields;
import com.planner.api.entity.Log;
import com.planner.api.entity.LogType;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@EntityScan(basePackages = "com.planner.api.entity")
class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private Template template;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
                User.builder().name("Test User").build()
        );

        template = Template.builder()
                .title("Test Template")
                .user(user)
                .auditFields(new AuditFields())
                .build();

        template = templateRepository.saveAndFlush(template);
    }

    @Test
    void shouldFindLogsByTemplateOrderedByPosition() {
        Log log1 = logRepository.save(
                new Log(template, LogType.TEXT, "Label 1", "Value 1", 1)
        );
        Log log2 = logRepository.save(
                new Log(template, LogType.TEXT, "Label 2", "Value 2", 0)
        );

        List<Log> result =
                logRepository.findByTemplateOrderByPositionAsc(template);

        assertThat(result).containsExactly(log2, log1);
    }

    @Test
    void shouldFindLogsByTemplateAndTypeOrderedByPosition() {
        Log log1 = logRepository.save(
                new Log(template, LogType.TEXT, "Text 1", "Value 1", 0)
        );
        Log log2 = logRepository.save(
                new Log(template, LogType.TEXT, "Text 2", "Value 2", 1)
        );
        Log log3 = logRepository.save(
                new Log(template, LogType.NUMBER, "Number", "123", 2)
        );

        List<Log> result =
                logRepository.findByTemplateAndTypeOrderByPositionAsc(
                        template,
                        LogType.TEXT
                );

        assertThat(result).containsExactly(log1, log2);
        assertThat(result).doesNotContain(log3);
    }
}
