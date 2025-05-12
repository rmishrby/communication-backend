package com.example.distribution.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
}
