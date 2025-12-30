package com.planner.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.planner.api.entity.User;
import com.planner.api.repository.UserRepository;
import com.planner.api.repository.EventRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void shouldCreateUpdateAndDeleteUser() throws Exception {
        // Create user
        mockMvc.perform(post("/users")
                        .param("username", "stephen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User user = userRepository.findByUsername("stephen").orElseThrow();

        // Update username
        mockMvc.perform(put("/users/" + user.getId())
                        .param("username", "stephen_updated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getUsername()).isEqualTo("stephen_updated");

        // Delete user
        mockMvc.perform(delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        boolean exists = userRepository.existsById(user.getId());
        assertThat(exists).isFalse();
    }
}
