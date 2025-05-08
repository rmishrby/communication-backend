package com.example.distribution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> taggedUsers = new HashSet<>();

    public ProjectUpdate() {
        this.createdAt = LocalDateTime.now();
    }

}
