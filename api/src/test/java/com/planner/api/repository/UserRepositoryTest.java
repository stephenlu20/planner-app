package com.planner.api.repository;

import com.planner.api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserById() {
        // given
        User user = User.builder()
                .name("Test User")
                .build();

        // when
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    void shouldReturnAllUsers() {
        // given
        userRepository.save(User.builder()
                .name("User 1")
                .build());
        userRepository.save(User.builder()
                .name("User 2")
                .build());

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldDeleteUserById() {
        // given
        User user = User.builder()
                .name("Test User")
                .build();
        userRepository.save(user);
        UUID userId = user.getId();

        // when
        userRepository.deleteById(userId);
        Optional<User> deletedUser = userRepository.findById(userId);

        // then
        assertThat(deletedUser).isEmpty();
    }
}
