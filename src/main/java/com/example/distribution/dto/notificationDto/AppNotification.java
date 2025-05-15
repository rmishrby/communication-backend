package com.example.distribution.dto.notificationDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AppNotification {
    private int id;
    private String title;
    private String body;
    private long timestamp;
    private boolean seen = false;
}
