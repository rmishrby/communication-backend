package com.example.distribution.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MeetingResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private String notes;
    private String createdBy;
    private List<ActionItemDto> actionItems;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ActionItemDto {
        private String text;
        private boolean done;
    }
}
