package com.example.distribution.repository;

import com.example.distribution.entity.ProjectUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Long> {
    List<ProjectUpdate> findAllByOrderByCreatedAtDesc();
}