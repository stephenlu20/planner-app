package com.planner.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Template {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Able to define template hierarchy (aka, template can have a template)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_template_id")
    private Template parentTemplate;

    @OneToMany(
            mappedBy = "parentTemplate",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("index ASC")
    private List<Template> childTemplates = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    // User can define ordering
    @Column(nullable = false)
    private int index;

    @Embedded
    private AuditFields auditFields = new AuditFields();

    @OneToMany(
            mappedBy = "template",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("index ASC")
    private List<Log> logs = new ArrayList<>();

    // Constructors
    public Template(User user, String title, int index) {
        this.user = user;
        this.title = title;
        this.index = index;
    }

    private Template(User user, Template parentTemplate, String title, int index) {
        this.user = user;
        this.parentTemplate = parentTemplate;
        this.title = title;
        this.index = index;
    }

    // Class Methods
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void reorder(int newIndex) {
        this.index = newIndex;
    }

    public Template addChildTemplate(String title) {
        Template child = new Template(
                this.user,
                this,
                title,
                childTemplates.size()
        );
        childTemplates.add(child);
        return child;
    }

    public void removeChildTemplate(Template child) {
        childTemplates.remove(child);
    }

    public boolean isTopLevel() {
        return parentTemplate == null;
    }

    public boolean isChildTemplate() {
        return parentTemplate != null;
    }
}

