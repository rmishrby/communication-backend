package com.example.distribution.repository;

import com.example.distribution.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}