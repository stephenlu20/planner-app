package com.planner.api.service;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import com.planner.api.repository.CalendarRepository;
import com.planner.api.repository.EventRepository;
import com.planner.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldToggleCompletedStatus() {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));

        Event event = eventRepository.save(new Event(user, calendar, "Test event", 1));

        assertThat(event.isCompleted()).isFalse();

        Event updated = eventService.toggleCompleted(event.getId());

        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void shouldCreateEvent() {
        User user = userRepository.save(new User("alice"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Work"));
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 1, 10, 0);
        Event event = eventService.createEvent("Meeting", "Review application diagram", dateTime,  1, user.getId(), calendar.getId());

        assertThat(event.getTitle()).isEqualTo("Meeting");
        assertThat(event.getNote()).isEqualTo("Review application diagram");
        assertThat(event.getDateTime()).isEqualTo(dateTime);
        assertThat(event.getOrderIndex()).isEqualTo(1);
        assertThat(event.getUser().getId()).isEqualTo(user.getId());
        assertThat(event.getCalendar().getId()).isEqualTo(calendar.getId());

    }
}
