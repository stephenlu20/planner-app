package com.planner.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    // Hierarchy. Events can have sub-events
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_event_id")
    private Event parentEvent;

    @OneToMany(
            mappedBy = "parentEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position ASC")
    private List<Event> subTasks = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    // For events at the top of the hierarchy
    @Column
    private Instant date;

    // nullable for sub-events
    @Column
    private Boolean isCompleted = false;

    @Column(columnDefinition = "TEXT")
    private String noteText;

    // Ording in calendar or sub-events
    @Column(nullable = false)
    private int position;

    @Column
    private Instant completedAt;

    @Embedded
    private AuditFields auditFields = new AuditFields();

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position ASC")
    private List<Log> logs = new ArrayList<>();

    // Constructor
    public Event(Calendar calendar, String title, Instant date, int position) {
        this.calendar = calendar;
        this.title = title;
        this.date = date;
        this.position = position;
        this.isCompleted = false;
    }

    public Event(Calendar calendar, Event parentEvent, String title, Instant date, int position) {
        this.calendar = calendar;
        this.parentEvent = parentEvent;
        this.title = title;
        this.date = date;
        this.position = position;
    }

    // Sub task constructor
    private Event(Event parentEvent, String title, int position) {
        this.parentEvent = parentEvent;
        this.calendar = parentEvent.calendar;
        this.title = title;
        this.position = position;
    }

    // Class methods
    public void updateTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setDate(Instant date) {
        assertTopLevel();
        this.date = date;
    }

    public void complete() {
        assertTopLevel();
        this.isCompleted = true;
        this.completedAt = Instant.now();
    }

    public void uncomplete() {
        assertTopLevel();
        this.isCompleted = false;
        this.completedAt = null;
    }

    public void addNote(String text) {
        this.noteText = text;
    }

    public void updateNote(String text) {
        this.noteText = text;
    }

    public void deleteNote() {
        this.noteText = null;
    }

    public Event addSubTask(String title) {
        Event child = new Event(
                this,
                title,
                subTasks.size()
        );
        subTasks.add(child);
        return child;
    }

    public void removeSubTask(Event subTask) {
        subTasks.remove(subTask);
    }

    public void reorderSubTasks(List<UUID> orderedIds) {
        for (int i = 0; i < orderedIds.size(); i++) {
            final int idx = i;
            UUID id = orderedIds.get(i);
            subTasks.stream()
                    .filter(e -> e.id.equals(id))
                    .findFirst()
                    .ifPresent(e -> e.position = idx);
        }
    }

    public boolean isTopLevel() {
        return parentEvent == null;
    }

    public boolean isSubTask() {
        return parentEvent != null;
    }

    private void assertTopLevel() {
        if (isSubTask()) {
            throw new IllegalStateException("Operation only allowed on top-level events");
        }
    }

    public void updateDate(Instant date) {
        this.date = date;
    }

    public void reorder(int newPosition) {
        this.position = newPosition;
    }
}
