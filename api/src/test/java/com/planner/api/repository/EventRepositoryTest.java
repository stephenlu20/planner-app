package com.planner.api.repository;

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
    private EventRepository eventRepository;

    @Test
    void shouldReturnEventsOrderedByOrderIndex() {
        // given
        User user = userRepository.save(new User("stephen"));

        Event e1 = new Event(user, "First", 2);
        Event e2 = new Event(user, "Second", 1);
        Event e3 = new Event(user, "Third", 3);

        eventRepository.saveAll(List.of(e1, e2, e3));

        // when
        List<Event> events =
                eventRepository.findByUserOrderByOrderIndexAsc(user);

        // then
        assertThat(events).hasSize(3);
        assertThat(events.get(0).getNote()).isEqualTo("Second");
        assertThat(events.get(1).getNote()).isEqualTo("First");
        assertThat(events.get(2).getNote()).isEqualTo("Third");
    }

    @Test
    void shouldFilterByCompletionStatus() {
        // given
        User user = userRepository.save(new User("stephen"));

        Event completed = new Event(user, "Done", 1);
        completed.setCompleted(true);

        Event pending = new Event(user, "Todo", 2);

        eventRepository.saveAll(List.of(completed, pending));

        // when
        List<Event> completedEvents =
                eventRepository.findByUserAndCompleted(user, true);

        // then
        assertThat(completedEvents).hasSize(1);
        assertThat(completedEvents.get(0).getNote()).isEqualTo("Done");
    }
}
