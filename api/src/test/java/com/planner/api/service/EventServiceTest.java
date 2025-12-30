package com.planner.api.service;

import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import com.planner.api.repository.EventRepository;
import com.planner.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldToggleCompletedStatus() {
        User user = userRepository.save(new User("stephen"));
        Event event = eventRepository.save(new Event(user, "Test event", 1));

        assertThat(event.isCompleted()).isFalse();

        Event updated = eventService.toggleCompleted(event.getId());

        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void shouldCreateEvent() {
        User user = userRepository.save(new User("alice"));

        Event event = eventService.createEvent("Meeting", 1, user.getId());

        assertThat(event.getNote()).isEqualTo("Meeting");
        assertThat(event.getOrderIndex()).isEqualTo(1);
        assertThat(event.getUser().getId()).isEqualTo(user.getId());
    }
}
