package com.example.distribution.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ProjectUpdateRequest {
    private String title;
    private String content;
    private Set<String> taggedUsers;
}
