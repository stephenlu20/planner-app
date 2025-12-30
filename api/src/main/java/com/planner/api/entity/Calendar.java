package com.planner.api.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    private String name;

    private int orderIndex;

    protected Calendar() { }

    public Calendar(User user, String name) {
        this.user = user;
        this.name = name;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
}
