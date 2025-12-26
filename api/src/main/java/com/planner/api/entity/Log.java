package com.planner.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Log {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Enumerated(EnumType.STRING)
    private LogType type;

    private String label;

    @Column(columnDefinition = "TEXT")
    private String value; // storing as JSON string or plain string

    @ElementCollection
    @CollectionTable(name = "log_metadata", joinColumns = @JoinColumn(name = "log_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @Builder.Default
    private Map<String, String> metadata = new HashMap<>();

    private int position;

    @Embedded
    private AuditFields audit;

    // Convenience constructor for event logs
    public Log(Event event, LogType type, String label, String value, int position) {
        this.event = event;
        this.type = type;
        this.label = label;
        this.value = value;
        this.position = position;
        this.audit = new AuditFields();
        this.metadata = new HashMap<>();
    }

    // Convenience constructor for template logs
    public Log(Template template, LogType type, String label, String value, int position) {
        this.template = template;
        this.type = type;
        this.label = label;
        this.value = value;
        this.position = position;
        this.audit = new AuditFields();
        this.metadata = new HashMap<>();
    }

}
