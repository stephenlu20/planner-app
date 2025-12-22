package com.planner.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "calendars")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Calendar {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    //User-defined ordering.
    @Column(nullable = false)
    private int index;

    @Embedded
    private AuditFields auditFields = new AuditFields();

    @OneToMany(
            mappedBy = "calendar",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("index ASC")
    private List<Event> events = new ArrayList<>();

    // Constructor
    public Calendar(User user, String name, String color, int index) {
        this.user = user;
        this.name = name;
        this.color = color;
        this.index = index;
    }

    // Methods
    public void rename(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void reorder(int newIndex) {
        this.index = newIndex;
    }

    public Event createEvent(String title, LocalDate date) {
        Event event = new Event(this, title, date, events.size());
        events.add(event);
        return event;
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }
}
