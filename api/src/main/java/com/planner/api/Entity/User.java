package com.planner.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requires at least protected
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // required for @Builder
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Builder.Default
    private AuditFields audit = new AuditFields();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Template> templates = new ArrayList<>();

    // constructor
    public User(String name) {
        this.name = name;
    }

    // Methods

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

    public void setName(String name) {
        this.name = name;
    }
}
