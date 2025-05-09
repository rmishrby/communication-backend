package com.example.distribution.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MeetingDto {
    private String title;
    private String notes;
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