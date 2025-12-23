package com.planner.api.repository;

import com.planner.api.entity.Log;
import com.planner.api.entity.LogType;
import com.planner.api.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {

    // Fetch all logs for a given template, ordered by position
    List<Log> findByTemplateOrderByPositionAsc(Template template);

    // Fetch logs by type for a template (if LogType exists)
    List<Log> findByTemplateAndTypeOrderByPositionAsc(Template template, LogType type);
}
