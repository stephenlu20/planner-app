package com.planner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planner.api.dto.EntryRequestDTO;
import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import com.planner.api.entity.EntryType;
import com.planner.api.entity.User;
import com.planner.api.service.EntryService;
import com.planner.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EntryControllerTest {

    private MockMvc mockMvc;
    private EntryService entryService;
    private UserService userService;
    private ObjectMapper objectMapper;

    private Long userId;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        entryService = mock(EntryService.class);
        userService = mock(UserService.class);
        EntryController controller = new EntryController(entryService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        userId = 1L;
        dummyUser = new User("Test User");
        dummyUser.setId(userId);
    }

    @Test
    void testCreateEntry() throws Exception {
        EntryRequestDTO dto = new EntryRequestDTO();
        dto.setType(EntryType.TEXT);
        dto.setSubjectType(EntrySubjectType.EVENT);
        dto.setSubjectId(UUID.randomUUID());
        dto.setLabel("Duration");
        dto.setValue("100 minutes");

        Entry saved = new Entry(dummyUser, dto.getType(), dto.getSubjectType(),
                dto.getSubjectId(), dto.getLabel(), dto.getValue());

        when(userService.getUser(userId)).thenReturn(dummyUser);
        when(entryService.createEntry(any(Entry.class))).thenReturn(saved);

        mockMvc.perform(post("/entries/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Duration"))
                .andExpect(jsonPath("$.value").value("100 minutes"))
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(entryService, times(1)).createEntry(any(Entry.class));
    }
}
