package com.planner.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String note;

    private LocalDateTime dateTime;

    private boolean completed;

    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    protected Event() {
    }

    public Event(
            String title,
            String note,
            LocalDateTime dateTime,
            User user,
            Calendar calendar,
            int orderIndex
    ) {
        this.title = title;
        this.note = note;
        this.dateTime = dateTime;
        this.user = user;
        this.calendar = calendar;
        this.orderIndex = orderIndex;
        this.completed = false;
    }

    public Event(User user, Calendar calendar, String note, int orderIndex) {
        this.user = user;
        this.calendar = calendar;
        this.note = note;
        this.orderIndex = orderIndex;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public User getUser() {
        return user;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
