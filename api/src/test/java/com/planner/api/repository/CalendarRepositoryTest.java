package com.planner.api.repository;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CalendarRepositoryTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve calendars for a user ordered by position")
    void shouldFindCalendarsByUserOrderedByPosition() {
        // Arrange
        User user = userRepository.save(new User("Stephen"));

        Calendar calendar1 = new Calendar(user, "Work", "blue", 1);
        Calendar calendar2 = new Calendar(user, "Personal", "green", 0);

        calendarRepository.save(calendar1);
        calendarRepository.save(calendar2);

        // Act
        List<Calendar> calendars =
                calendarRepository.findByUserOrderByPositionAsc(user);

        // Assert
        assertThat(calendars).hasSize(2);
        assertThat(calendars.get(0).getName()).isEqualTo("Personal");
        assertThat(calendars.get(1).getName()).isEqualTo("Work");
    }

    @Test
    @DisplayName("Should return empty list when user has no calendars")
    void shouldReturnEmptyListWhenNoCalendarsExist() {
        // Arrange
        User user = userRepository.save(new User("Empty User"));

        // Act
        List<Calendar> calendars =
                calendarRepository.findByUserOrderByPositionAsc(user);

        // Assert
        assertThat(calendars).isEmpty();
    }
}
