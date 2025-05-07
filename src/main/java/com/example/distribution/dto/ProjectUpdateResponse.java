package com.example.distribution.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectUpdateResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> taggedUsers;

}
