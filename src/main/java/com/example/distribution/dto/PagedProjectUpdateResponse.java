package com.example.distribution.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PagedProjectUpdateResponse {
    private List<ProjectUpdateResponse> updates;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
