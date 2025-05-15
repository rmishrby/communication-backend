package com.example.distribution.dto.notificationDto;

import lombok.Data;

@Data
public class SubscribeRequest {
    private String userId;
    private String token;
}
