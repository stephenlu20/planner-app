package com.planner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planner.api.dto.TemplateRequestDTO;
import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import com.planner.api.service.TemplateService;
import com.planner.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TemplateControllerTest {

    private MockMvc mockMvc;
    private TemplateService templateService;
    private UserService userService;
    private ObjectMapper objectMapper;

    private Long userId;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        templateService = mock(TemplateService.class);
        userService = mock(UserService.class);
        TemplateController controller = new TemplateController(templateService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        userId = 1L;
        dummyUser = new User("Test User");
        dummyUser.setId(userId);
    }

    @Test
    void testCreateTemplate() throws Exception {
        TemplateRequestDTO dto = new TemplateRequestDTO();
        dto.setName("Template 1");
        dto.setNote("This is a note");

        Template saved = new Template(UUID.randomUUID(), "Template 1", "This is a note",
                dummyUser);

        when(userService.getUser(userId)).thenReturn(dummyUser);
        when(templateService.createTemplate(any(Template.class), eq(dummyUser))).thenReturn(saved);

        System.out.println("/templates/user/" + userId.toString());
        mockMvc.perform(post("/templates/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Template 1"))
                .andExpect(jsonPath("$.note").value("This is a note"))
                .andExpect(jsonPath("$.ownerId").value(userId.toString()));

        verify(templateService, times(1)).createTemplate(any(Template.class), eq(dummyUser));
    }

    @Test
    void testGetTemplate() throws Exception {
        UUID templateId = UUID.randomUUID();
        Template template = new Template(templateId, "Template 2", "Note 2",
                dummyUser);

        when(templateService.getTemplate(templateId)).thenReturn(template);

        mockMvc.perform(get("/templates/" + templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Template 2"))
                .andExpect(jsonPath("$.note").value("Note 2"));
    }

    @Test
    void testGetTemplatesByUser() throws Exception {
        Template t1 = new Template(UUID.randomUUID(), "T1", "Note1", dummyUser);
        Template t2 = new Template(UUID.randomUUID(), "T2", "Note2", dummyUser);

        when(templateService.getTemplatesByUser(userId)).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/templates/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("T1"))
                .andExpect(jsonPath("$[1].name").value("T2"));
    }

    @Test
    void testUpdateTemplate() throws Exception {
        UUID templateId = UUID.randomUUID();
        TemplateRequestDTO dto = new TemplateRequestDTO();
        dto.setName("Updated Template");
        dto.setNote("Updated note");

        Template updated = new Template(templateId, "Updated Template", "Updated note",
                dummyUser);

        when(templateService.updateTemplate(eq(templateId), any(Template.class))).thenReturn(updated);

        mockMvc.perform(put("/templates/" + templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Template"))
                .andExpect(jsonPath("$.note").value("Updated note"));
    }

    @Test
    void testDeleteTemplate() throws Exception {
        UUID templateId = UUID.randomUUID();
        doNothing().when(templateService).deleteTemplate(templateId);

        mockMvc.perform(delete("/templates/" + templateId))
                .andExpect(status().isOk());

        verify(templateService, times(1)).deleteTemplate(templateId);
    }
}
