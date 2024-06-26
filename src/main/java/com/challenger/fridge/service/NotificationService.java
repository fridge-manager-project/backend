package com.challenger.fridge.service;

import com.challenger.fridge.domain.notification.Notice;
import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.domain.notification.StorageNotification;
import com.challenger.fridge.dto.notification.NoticeResponse;
import com.challenger.fridge.dto.notification.NotificationResponse;
import com.challenger.fridge.dto.notification.StorageNotificationResponse;
import com.challenger.fridge.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse findAllNotification(String email) {
        List<StorageNotification> storageNotificationsByEmail = notificationRepository.findStorageNotificationByEmail(email);
        List<StorageNotificationResponse> storageNotificationResponses = storageNotificationsByEmail.stream()
                .map(StorageNotificationResponse::from)
                .collect(Collectors.toList());

        List<Notice> noticeByEmail = notificationRepository.findNoticeByEmail(email);
        List<NoticeResponse> noticeResponses = noticeByEmail.stream()
                .map(NoticeResponse::from)
                .collect(Collectors.toList());

        return new NotificationResponse(storageNotificationResponses, noticeResponses);
    }

    @Transactional
    public Long readNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        notification.read();
        return notification.getId();
    }
}
