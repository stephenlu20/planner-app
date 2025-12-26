package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.Event;
import com.planner.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    private User user;
    private Calendar calendar;

    @BeforeEach
    void setup() {
        // Persist user
        user = userRepository.saveAndFlush(
                User.builder()
                        .name("Test User")
                        .build()
        );

        // Persist calendar EXPLICITLY
        calendar = calendarRepository.saveAndFlush(
                new Calendar(user, "Work", "Blue", 0)
        );
    }

    @Test
    void shouldSaveAndFindEventById() {
        Event event = eventRepository.saveAndFlush(
                new Event(calendar, "Meeting", Instant.now(), 0)
        );

        Optional<Event> found = eventRepository.findById(event.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Meeting");
    }

    @Test
    void shouldReturnEventsOrderedByPosition() {
        Event e1 = eventRepository.save(new Event(calendar, "Event 1", Instant.now(), 1));
        Event e2 = eventRepository.save(new Event(calendar, "Event 2", Instant.now(), 0));

        List<Event> events =
                eventRepository.findByCalendarOrderByPositionAsc(calendar);

        assertThat(events).containsExactly(e2, e1);
    }

    @Test
    void shouldFindEventsByInstant() {
        Instant now = Instant.now();
        Instant start = now.minusSeconds(1);
        Instant end = now.plusSeconds(1);

        Event event = eventRepository.saveAndFlush(
                new Event(calendar, "Instant Event", now, 0)
        );

        List<Event> result =
                eventRepository.findByCalendarAndDateBetween(calendar, start, end);

        assertThat(result).containsExactly(event);
    }


    @Test
    void shouldFindEventsBetweenInstants() {
        Instant start = Instant.now();
        Instant middle = start.plusSeconds(3600);
        Instant end = start.plusSeconds(7200);
        Instant before = start.minusSeconds(3600);

        Event e1 = eventRepository.save(new Event(calendar, "A", middle, 0));
        Event e2 = eventRepository.save(new Event(calendar, "B", end, 1));
        Event e3 = eventRepository.save(new Event(calendar, "C", before, 2));

        List<Event> result =
                eventRepository.findByCalendarAndDateGreaterThanEqualAndDateLessThan(calendar, start, end);

        assertThat(result).containsExactlyInAnyOrder(e1, e2);
        assertThat(result).doesNotContain(e3);
    }

    @Test
    void shouldHandleParentAndChildEvents() {
        Event parent = eventRepository.saveAndFlush(
                new Event(calendar, "Parent", Instant.now(), 0)
        );

        Event child1 = eventRepository.save(
                new Event(calendar, parent, "Child 1", Instant.now(), 0)
        );

        Event child2 = eventRepository.save(
                new Event(calendar, parent, "Child 2", Instant.now(), 1)
        );

        List<Event> topLevel =
                eventRepository.findByCalendarAndParentEventIsNullOrderByPositionAsc(calendar);

        List<Event> children =
                eventRepository.findByParentEventOrderByPositionAsc(parent);

        assertThat(topLevel).containsExactly(parent);
        assertThat(children).containsExactly(child1, child2);
    }
}
