package com.planner.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private AuditFields audit = new AuditFields();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Template> templates = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    // ---------- Domain Methods ----------

    public Calendar createCalendar(String name, String color) {
        Calendar calendar = new Calendar(this, name, color, calendars.size());
        calendars.add(calendar);
        return calendar;
    }

    public Template createTemplate(String title) {
        Template template = new Template(this, title, templates.size());
        templates.add(template);
        return template;
    }

    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);
    }

    public void removeTemplate(Template template) {
        templates.remove(template);
    }
}

