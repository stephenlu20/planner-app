package com.planner.api.service;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import com.planner.api.repository.CalendarRepository;
import com.planner.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalendarServiceTest {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    void shouldCreateCalendar() {
        User user = userRepository.save(new User("alice"));

        Calendar calendar = calendarService.createCalendar(user.getId(), "Work", 0);

        assertThat(calendar.getName()).isEqualTo("Work");
        assertThat(calendar.getOrderIndex()).isEqualTo(0);
        assertThat(calendar.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldReorderCalendars() {
        User user = userRepository.save(new User("bob"));

        Calendar c1 = new Calendar(user, "First");
        Calendar c2 = new Calendar(user, "Second");
        Calendar c3 = new Calendar(user, "Third");

        // set initial orderIndex before saving
        c1.setOrderIndex(0);
        c2.setOrderIndex(1);
        c3.setOrderIndex(2);

        calendarRepository.saveAll(List.of(c1, c2, c3));

        // reorder: c3 -> c1 -> c2
        calendarService.reorderCalendars(
                user.getId(),
                List.of(c3.getId(), c1.getId(), c2.getId())
        );

        // reload from repository
        List<Calendar> updated = calendarRepository.findByUserOrderByOrderIndexAsc(user);

        // map IDs to orderIndex for robust assertions
        var idToIndex = updated.stream()
                .collect(Collectors.toMap(Calendar::getId, Calendar::getOrderIndex));

        assertThat(idToIndex.get(c3.getId())).isEqualTo(0);
        assertThat(idToIndex.get(c1.getId())).isEqualTo(1);
        assertThat(idToIndex.get(c2.getId())).isEqualTo(2);
    }

    @Test
    void shouldDeleteCalendar() {
        User user = userRepository.save(new User("charlie"));
        Calendar calendar = calendarRepository.save(new Calendar(user, "Temp"));

        calendarService.deleteCalendar(calendar.getId());

        assertThat(calendarRepository.existsById(calendar.getId())).isFalse();
    }
}
