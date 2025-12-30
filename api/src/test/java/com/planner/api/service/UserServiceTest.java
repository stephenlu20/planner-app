package com.planner.api.service;

import com.planner.api.entity.User;
import com.planner.api.repository.EventRepository;
import com.planner.api.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void cleanUp() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchUserByUsername() {
        User user = userService.createUser("stephen");

        User found = userService.getUserByUsername("stephen");
        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldFetchUserById() {
        User user = userService.createUser("alice");

        User found = userService.getUser(user.getId());
        assertThat(found.getUsername()).isEqualTo("alice");
    }
}
