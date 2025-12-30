package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldReturnEventsOrderedByOrderIndex() {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));

        Event e1 = new Event(user, calendar, "First", 2);
        Event e2 = new Event(user, calendar, "Second", 1);
        Event e3 = new Event(user, calendar, "Third", 3);

        eventRepository.saveAll(List.of(e1, e2, e3));

        List<Event> events = eventRepository.findByUserOrderByOrderIndexAsc(user);

        assertThat(events).hasSize(3);
        assertThat(events.get(0).getNote()).isEqualTo("Second");
        assertThat(events.get(1).getNote()).isEqualTo("First");
        assertThat(events.get(2).getNote()).isEqualTo("Third");
    }

    @Test
    void shouldFilterByCompletionStatus() {
        User user = userRepository.save(new User("stephen"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Default"));

        Event completed = new Event(user, calendar, "Done", 1);
        completed.setCompleted(true);

        Event pending = new Event(user, calendar, "Todo", 2);

        eventRepository.saveAll(List.of(completed, pending));

        List<Event> completedEvents = eventRepository.findByUserAndCompleted(user, true);

        assertThat(completedEvents).hasSize(1);
        assertThat(completedEvents.get(0).getNote()).isEqualTo("Done");
    }
}
