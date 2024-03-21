package com.challenger.fridge.service;

import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.domain.notification.StorageNotification;
import com.challenger.fridge.dto.notification.NotificationResponse;
import com.challenger.fridge.dto.notification.StorageNotificationResponse;
import com.challenger.fridge.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse findAllNotification(String email) {
        List<StorageNotification> storageNotificationsByEmail = notificationRepository.findStorageNotificationByEmail(email);
        List<StorageNotificationResponse> storageNotificationResponses = storageNotificationsByEmail.stream()
                .map(StorageNotificationResponse::from)
                .collect(Collectors.toList());


        return new NotificationResponse(storageNotificationResponses);
    }
}
