package com.planner.api.repository;

import com.planner.api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByUsername() {
        // given
        User user = new User("stephen");
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByUsername("stephen");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("stephen");
    }
}
