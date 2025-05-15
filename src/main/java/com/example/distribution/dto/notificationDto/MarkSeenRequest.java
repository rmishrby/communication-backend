package com.example.distribution.dto.notificationDto;


import lombok.Data;

@Data
public class MarkSeenRequest {
    private String userId;
    private int notificationId;
}