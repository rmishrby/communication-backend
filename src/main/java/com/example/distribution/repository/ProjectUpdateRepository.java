package com.example.distribution.repository;

import com.example.distribution.entity.ProjectUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Long> {
    Page<ProjectUpdate> findAllByOrderByCreatedAtDesc(Pageable pageable);
}