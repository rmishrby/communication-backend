package com.example.distribution.controller;

import com.example.distribution.dto.notificationDto.MarkSeenRequest;
import com.example.distribution.dto.notificationDto.AppNotification;
import com.example.distribution.dto.notificationDto.NotificationRequest;
import com.example.distribution.dto.notificationDto.SubscribeRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.messaging.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
@CrossOrigin
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final Map<String, List<AppNotification>> notifications = new ConcurrentHashMap<>();

    private int generateNotificationId() {
        return new Random().nextInt(9000) + 1000;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscribeRequest request) {
        tokens.put(request.getUserId(), request.getToken());
        logger.info("tokens :", toString());
        notifications.putIfAbsent(request.getUserId(), new ArrayList<>());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request) {
        String token = tokens.get(request.getUserId());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }

        int notificationId = generateNotificationId();
        AppNotification notif = new AppNotification(notificationId, request.getTitle(), request.getBody(), System.currentTimeMillis(), false);
        Message message = Message.builder()
                .setToken(token)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .putData("id", String.valueOf(notificationId))
                .putData("body", request.getBody())
                .putData("seen", "false")
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
            notifications.computeIfAbsent(request.getUserId(), k -> new ArrayList<>()).add(notif);
            return ResponseEntity.ok().build();
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending notification");
        }
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<AppNotification>> getUnseen(@PathVariable String userId) {
        List<AppNotification> userNotifs = notifications.getOrDefault(userId, new ArrayList<>());
        List<AppNotification> unseen = userNotifs.stream().filter(n -> !n.isSeen()).toList();
        return ResponseEntity.ok(unseen);
    }

    @PostMapping("/notifications/mark-seen")
    public ResponseEntity<?> markSeen(@RequestBody MarkSeenRequest request) {
        List<AppNotification> userNotifs = notifications.get(request.getUserId());
        if (userNotifs == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<AppNotification> notif = userNotifs.stream().filter(n -> n.getId() == request.getNotificationId()).findFirst();
        if (notif.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
        }

        notif.get().setSeen(true);
        return ResponseEntity.ok(Map.of("success", true, "message", "Notification marked as seen."));
    }
}
