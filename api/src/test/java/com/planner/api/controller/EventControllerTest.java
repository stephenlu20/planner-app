package com.planner.api.controller;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import com.planner.api.repository.CalendarRepository;
import com.planner.api.repository.EventRepository;
import com.planner.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldToggleEventCompletion() throws Exception {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));
        Event event = eventRepository.save(new Event(user, calendar, "Test", 1));

        mockMvc.perform(
                put("/events/" + event.getId() + "/toggle")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldCreateEvent() throws Exception {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));

        String json = """
            {
                "note": "New Event",
                "orderIndex": 1,
                "userId": "%s",
                "calendarId": "%s"
            }
            """.formatted(user.getId(), calendar.getId());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReorderEvents() throws Exception {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));

        Event e1 = eventRepository.save(new Event(user, calendar, "First", 1));
        Event e2 = eventRepository.save(new Event(user, calendar, "Second", 2));
        Event e3 = eventRepository.save(new Event(user, calendar, "Third", 3));

        String json = """
            {
                "calendarId": "%s",
                "orderedEventIds": ["%s", "%s", "%s"]
            }
            """.formatted(
                calendar.getId(),
                e3.getId(),
                e1.getId(),
                e2.getId()
        );

        mockMvc.perform(put("/events/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        List<Event> updated = eventRepository.findByCalendarIdOrderByOrderIndexAsc(calendar.getId());
        assertThat(updated.get(0).getId()).isEqualTo(e3.getId());
        assertThat(updated.get(1).getId()).isEqualTo(e1.getId());
        assertThat(updated.get(2).getId()).isEqualTo(e2.getId());
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));
        Event event = eventRepository.save(new Event(user, calendar, "To be deleted", 1));

        mockMvc.perform(delete("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        boolean exists = eventRepository.existsById(event.getId());
        assertThat(exists).isFalse();
    }
}
