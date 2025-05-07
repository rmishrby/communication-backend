package com.example.distribution.service;

import com.example.distribution.entity.ProjectUpdate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void notifyTaggedUsers(ProjectUpdate projectUpdate) {
        for (String user : projectUpdate.getTaggedUsers()) {
            System.out.println(" Notification: @" + user + " was tagged in update: \"" + projectUpdate.getTitle() + "\"");
        }
    }
}

