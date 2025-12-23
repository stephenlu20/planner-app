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
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Calendar testCalendar;

    @BeforeEach
    void setup() {
        // Persist a test user
        testUser = userRepository.saveAndFlush(User.builder()
                .name("Test User")
                .build());

        // Explicitly create and persist a calendar
        testCalendar = new Calendar(testUser, "Work Calendar", "Blue", 0);
        testUser.getCalendars().add(testCalendar);
        userRepository.saveAndFlush(testUser); // ensures calendar is managed and has an ID
    }

    @Test
    void shouldSaveAndFindEventById() {
        // given
        Event event = new Event(testCalendar, "Meeting", Instant.now(), 0);

        // when
        Event savedEvent = eventRepository.saveAndFlush(event);
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());

        // then
        assertThat(foundEvent).isPresent();
        assertThat(foundEvent.get().getTitle()).isEqualTo("Meeting");
    }

    @Test
    void shouldReturnEventsOrderedByPosition() {
        // given
        Event e1 = eventRepository.save(new Event(testCalendar, "Event 1", Instant.now(), 1));
        Event e2 = eventRepository.save(new Event(testCalendar, "Event 2", Instant.now(), 0));

        // when
        List<Event> events = eventRepository.findByCalendarOrderByPositionAsc(testCalendar);

        // then
        assertThat(events).containsExactly(e2, e1); // ordered by position
    }

    @Test
    void shouldFindEventsByInstant() {
        // given
        Instant now = Instant.now();
        Event e1 = eventRepository.save(new Event(testCalendar, "Instant Event", now, 0));

        // when
        List<Event> eventsOnDate = eventRepository.findByCalendarAndDate(testCalendar, now);

        // then
        assertThat(eventsOnDate).containsExactly(e1);
    }

    @Test
    void shouldFindEventsBetweenInstants() {
        // given
        Instant start = Instant.now();
        Instant middle = start.plusSeconds(3600);
        Instant end = start.plusSeconds(7200);
        Instant before = start.minusSeconds(3600);

        Event e1 = eventRepository.save(new Event(testCalendar, "Event A", middle, 0));
        Event e2 = eventRepository.save(new Event(testCalendar, "Event B", end, 1));
        Event e3 = eventRepository.save(new Event(testCalendar, "Event C", before, 2));

        // when
        List<Event> eventsInRange = eventRepository.findByCalendarAndDateBetween(testCalendar, start, end);

        // then
        assertThat(eventsInRange).containsExactlyInAnyOrder(e1, e2);
        assertThat(eventsInRange).doesNotContain(e3);
    }

    @Test
    void shouldHandleParentAndChildEvents() {
        // given
        Event parent = eventRepository.save(new Event(testCalendar, "Parent Event", Instant.now(), 0));
        Event child1 = eventRepository.save(new Event(testCalendar, parent, "Child 1", Instant.now(), 0));
        Event child2 = eventRepository.save(new Event(testCalendar, parent, "Child 2", Instant.now(), 1));

        // when
        List<Event> topLevel = eventRepository.findByCalendarAndParentEventIsNullOrderByPositionAsc(testCalendar);
        List<Event> children = eventRepository.findByParentEventOrderByPositionAsc(parent);

        // then
        assertThat(topLevel).containsExactly(parent);
        assertThat(children).containsExactly(child1, child2);
    }
}
