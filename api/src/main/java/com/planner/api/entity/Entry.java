package com.planner.api.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private EntryType type;

    @Enumerated(EnumType.STRING)
    private EntrySubjectType subjectType;

    private UUID subjectId;

    private String label;
    private String value;

    private Instant createdAt = Instant.now();

    protected Entry() { }

    public Entry(User user, EntryType type, EntrySubjectType subjectType,
                 UUID subjectId, String label, String value) {
        this.user = user;
        this.type = type;
        this.subjectType = subjectType;
        this.subjectId = subjectId;
        this.label = label;
        this.value = value;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public EntryType getType() { return type; }
    public void setType(EntryType type) { this.type = type; }
    public EntrySubjectType getSubjectType() { return subjectType; }
    public void setSubjectType(EntrySubjectType subjectType) { this.subjectType = subjectType; }
    public UUID getSubjectId() { return subjectId; }
    public void setSubjectId(UUID subjectId) { this.subjectId = subjectId; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
