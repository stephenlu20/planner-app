package com.planner.api.repository;

import com.planner.api.entity.Entry;
import com.planner.api.entity.EntrySubjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EntryRepository extends JpaRepository<Entry, UUID> {

    List<Entry> findBySubjectTypeAndSubjectId(EntrySubjectType subjectType, UUID subjectId);

    List<Entry> findByUserId(UUID userId);
}
