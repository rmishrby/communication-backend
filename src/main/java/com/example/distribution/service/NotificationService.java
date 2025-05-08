package com.example.distribution.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.sns.topic.arn}")
    private String topicArn;

    public void sendNotification(String subject, String message) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .subject(subject)
                .message(message)
                .build();

        snsClient.publish(request);
    }
}
