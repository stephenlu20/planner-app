package com.planner.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @OrderBy("index ASC")
    private List<Event> subTasks = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    // For events at the top of the hierarchy
    @Column
    private LocalDate date;

    // nullable for sub-events
    @Column
    private Boolean isCompleted;

    @Column(columnDefinition = "TEXT")
    private String noteText;

    // Ording in calendar or sub-events
    @Column(nullable = false)
    private int index;

    @Column
    private LocalDateTime completedAt;

    @Embedded
    private AuditFields auditFields = new AuditFields();

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("index ASC")
    private List<Log> logs = new ArrayList<>();

    // Constructor
    public Event(Calendar calendar, String title, LocalDate date, int index) {
        this.calendar = calendar;
        this.title = title;
        this.date = date;
        this.index = index;
        this.isCompleted = false;
    }

    // Sub task constructor
    private Event(Event parentEvent, String title, int index) {
        this.parentEvent = parentEvent;
        this.calendar = parentEvent.calendar;
        this.title = title;
        this.index = index;
    }

    // Class methods
    public void updateTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        assertTopLevel();
        this.date = date;
    }

    public void complete() {
        assertTopLevel();
        this.isCompleted = true;
        this.completedAt = LocalDateTime.now();
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
                    .ifPresent(e -> e.index = idx);
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
}
