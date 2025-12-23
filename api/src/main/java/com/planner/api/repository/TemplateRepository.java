package com.planner.api.repository;

import com.planner.api.entity.Template;
import com.planner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TemplateRepository extends JpaRepository<Template, UUID> {

    // Fetch all top-level templates for a user, ordered by index
    List<Template> findByUserAndParentTemplateIsNullOrderByIndexAsc(User user);

    // Fetch all child templates of a parent template, ordered by index
    List<Template> findByParentTemplateOrderByIndexAsc(Template parentTemplate);

    // Check if a template exists by title for a specific user (top-level)
    boolean existsByUserAndParentTemplateIsNullAndTitle(User user, String title);
}

