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

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EntryControllerTest {

    private MockMvc mockMvc;
    private EntryService recordService;
    private UserService userService;
    private ObjectMapper objectMapper;

    private Long userId;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        recordService = mock(EntryService.class);
        userService = mock(UserService.class);
        EntryController controller = new EntryController(recordService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        userId = 1L;
        dummyUser = new User("Test User");
        dummyUser.setId(userId);
    }

    @Test
    void testCreateRecord() throws Exception {
        EntryRequestDTO dto = new EntryRequestDTO();
        dto.setType(EntryType.TEXT);
        dto.setSubjectType(EntrySubjectType.EVENT);
        dto.setSubjectId(UUID.randomUUID());
        dto.setLabel("Duration");
        dto.setValue("100 minutes");

        Entry saved = new Entry(dummyUser, dto.getType(), dto.getSubjectType(),
                dto.getSubjectId(), dto.getLabel(), dto.getValue());

        when(userService.getUser(userId)).thenReturn(dummyUser);
        when(recordService.createRecord(any(Entry.class))).thenReturn(saved);

        mockMvc.perform(post("/records/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Duration"))
                .andExpect(jsonPath("$.value").value("100 minutes"))
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(recordService, times(1)).createRecord(any(Entry.class));
    }

    @Test
    void testGetRecordsByUser() throws Exception {
        Entry r1 = new Entry(dummyUser, EntryType.TEXT, EntrySubjectType.EVENT, UUID.randomUUID(), "Duration", "100");
        Entry r2 = new Entry(dummyUser, EntryType.DURATION, EntrySubjectType.TEMPLATE, UUID.randomUUID(), "Default", "30");

        when(recordService.getRecordsByUser(userId)).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/records/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Duration"))
                .andExpect(jsonPath("$[1].label").value("Default"));
    }
}
