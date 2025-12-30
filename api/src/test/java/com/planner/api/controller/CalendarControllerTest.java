package com.planner.api.controller;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import com.planner.api.repository.CalendarRepository;
import com.planner.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    void shouldCreateCalendar() throws Exception {
        User user = userRepository.save(new User("stephen"));

        String json = """
            {
                "userId": "%s",
                "name": "Work",
                "orderIndex": 0
            }
            """.formatted(user.getId());

        mockMvc.perform(post("/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReorderCalendars() throws Exception {
        User user = userRepository.save(new User("alice"));

        Calendar c1 = calendarRepository.save(new Calendar(user, "First"));
        c1.setOrderIndex(0);
        Calendar c2 = calendarRepository.save(new Calendar(user, "Second"));
        c2.setOrderIndex(1);
        Calendar c3 = calendarRepository.save(new Calendar(user, "Third"));
        c3.setOrderIndex(2);

        String json = """
            {
                "userId": "%s",
                "orderedCalendarIds": ["%s", "%s", "%s"]
            }
            """.formatted(
                user.getId(),
                c3.getId(),
                c1.getId(),
                c2.getId()
        );

        mockMvc.perform(put("/calendars/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        List<Calendar> updated = calendarRepository.findByUserOrderByOrderIndexAsc(user);
        assertThat(updated.get(0).getId()).isEqualTo(c3.getId());
        assertThat(updated.get(1).getId()).isEqualTo(c1.getId());
        assertThat(updated.get(2).getId()).isEqualTo(c2.getId());
    }

    @Test
    void shouldDeleteCalendar() throws Exception {
        User user = userRepository.save(new User("bob"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Temp"));

        mockMvc.perform(delete("/calendars/" + calendar.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(calendarRepository.existsById(calendar.getId())).isFalse();
    }
}
