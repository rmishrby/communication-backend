package com.example.distribution.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUpdateResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Set<String> taggedUsers;
    private Long projectId;
    private String projectTitle;
}
