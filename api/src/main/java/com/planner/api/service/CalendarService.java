package com.planner.api.service;

import com.planner.api.entity.Calendar;
import com.planner.api.entity.User;
import com.planner.api.repository.CalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserService userService;

    public CalendarService(CalendarRepository calendarRepository, UserService userService) {
        this.calendarRepository = calendarRepository;
        this.userService = userService;
    }

    public Calendar getCalendar(UUID calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found"));
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }


    public List<Calendar> getCalendarsForUser(Long userId) {
        User user = userService.getUser(userId);
        return calendarRepository.findByUserOrderByOrderIndexAsc(user);
    }

    public Calendar createCalendar(Long userId, String name, int orderIndex) {
        User user = userService.getUser(userId);
        Calendar calendar = new Calendar(user, name);
        calendar.setOrderIndex(orderIndex);
        return calendarRepository.save(calendar);
    }

    public List<Calendar> reorderCalendars(Long userId, List<UUID> orderedCalendarIds) {
        User user = userService.getUser(userId);
        List<Calendar> calendars = calendarRepository.findByUserOrderByOrderIndexAsc(user);

        if (calendars.size() != orderedCalendarIds.size()) {
            throw new IllegalArgumentException("Mismatch in number of calendars");
        }

        var calendarMap = calendars.stream()
                .collect(Collectors.toMap(Calendar::getId, c -> c));

        for (int i = 0; i < orderedCalendarIds.size(); i++) {
            UUID id = orderedCalendarIds.get(i);
            Calendar c = calendarMap.get(id);
            if (c == null) {
                throw new IllegalArgumentException("Invalid calendar ID: " + id);
            }
            c.setOrderIndex(i);
        }

        return calendarRepository.saveAll(calendars);
    }

    public void deleteCalendar(UUID calendarId) {
        Calendar calendar = getCalendar(calendarId);
        calendarRepository.delete(calendar);
    }
}
