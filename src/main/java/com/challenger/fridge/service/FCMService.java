package com.challenger.fridge.service;

import com.challenger.fridge.dto.NotificationRequest;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.repository.FCMTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FCMService {

    private final FCMTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    public void saveToken(SignInRequest signInRequest, String deviceToken) {
        fcmTokenRepository.saveFCMToken(signInRequest.getEmail(), deviceToken);
    }

    public void sendTestNotification(NotificationRequest notificationRequest) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(notificationRequest.getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle(notificationRequest.getTitle())
                        .setBody(notificationRequest.getBody())
                        .build())
                .build();
        firebaseMessaging.send(message);
    }
}
