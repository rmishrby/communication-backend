package com.example.distribution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class ProjectUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private LocalDateTime createdAt;

    @ElementCollection
    private List<String> taggedUsers;

    public ProjectUpdate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
}
