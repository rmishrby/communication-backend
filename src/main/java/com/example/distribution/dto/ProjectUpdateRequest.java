package com.example.distribution.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ProjectUpdateRequest {
    private String title;
    private String content;
    private List<String> taggedUsers;
}
