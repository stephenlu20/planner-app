package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CalendarRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    void shouldReturnCalendarsOrderedByOrderIndex() {
        User user = userRepository.save(new User("stephen"));

        Calendar c1 = new Calendar(user, "Work");
        c1.setOrderIndex(2);
        Calendar c2 = new Calendar(user, "Personal");
        c2.setOrderIndex(1);
        Calendar c3 = new Calendar(user, "Other");
        c3.setOrderIndex(3);

        calendarRepository.saveAll(List.of(c1, c2, c3));

        List<Calendar> calendars = calendarRepository.findByUserOrderByOrderIndexAsc(user);

        assertThat(calendars).hasSize(3);
        assertThat(calendars.get(0).getName()).isEqualTo("Personal");
        assertThat(calendars.get(1).getName()).isEqualTo("Work");
        assertThat(calendars.get(2).getName()).isEqualTo("Other");
    }
}
