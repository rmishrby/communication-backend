package com.example.distribution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> taggedUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}

