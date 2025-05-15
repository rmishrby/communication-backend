package com.example.distribution.dto.notificationDto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String userId;
    private String title;
    private String body;
}