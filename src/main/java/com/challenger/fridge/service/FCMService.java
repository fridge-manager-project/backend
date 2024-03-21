package com.challenger.fridge.service;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.domain.notification.StorageNotification;
import com.challenger.fridge.dto.notification.NotificationRequest;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.repository.FCMTokenRepository;
import com.challenger.fridge.repository.NotificationRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FCMService {

    private final FCMTokenRepository fcmTokenRepository;
    private final StorageItemRepository storageItemRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final NotificationRepository notificationRepository;

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

    @Scheduled(cron = "0 0 15 * * *")
    public void sendEatableItemNotificationInStorage() throws FirebaseMessagingException {
        log.info("소비기한 임박 상품 푸시 알림 보내기 시작");

        // 유통기한이 임박한 상품을 가진 사용자 찾기
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        List<StorageItem> storageItemsByExpirationDateBetween =
                storageItemRepository.findStorageItemsByExpirationDateBetween(startDate, endDate);

        // 해당 사용자의 email 을 사용하여 deviceTokenList 만들기
        List<String> deviceTokenList = getDeviceTokenListFromStorageItems(storageItemsByExpirationDateBetween);

        if (deviceTokenList.isEmpty()) {
            log.info("deviceToken 이 비었습니다.");
            return;
        }

        // 해당 List 로 푸시 알림 보내기
        String title = "\uD83D\uDD25\uD83D\uDD25빨리 먹어야해요\uD83D\uDD25\uD83D\uDD25";
        String body = "소비 기간이 얼마 남지 않은 상품이 있어요. 냉장고를 확인 주세요!";
        MulticastMessage message = makeNotificationMessage(deviceTokenList, title, body);

        BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
        log.info("총 " + response.getSuccessCount() + " 개의 메시지 전송 성공");
        log.info("총 " + response.getFailureCount() + " 개의 메시지 전송 실패");

        if (response.getFailureCount() > 0) {
            checkFailedNotificationResponse(deviceTokenList, response);
        }

        saveItemExpirationNotification(storageItemsByExpirationDateBetween);
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendUnEatableItemNotificationInStorage() throws FirebaseMessagingException {
        log.info("소비기한 지난 상품 푸시 알림 보내기 시작");

        // 유통기한이 임박한 상품을 가진 사용자 찾기
        List<StorageItem> storageItemsAfterExpirationDate =
                storageItemRepository.findStorageItemsByExpirationDateBefore(LocalDate.now());

        // 해당 사용자의 email 을 사용하여 deviceTokenList 만들기
        List<String> deviceTokenList = getDeviceTokenListFromStorageItems(storageItemsAfterExpirationDate);

        if (deviceTokenList.isEmpty()) {
            log.info("deviceToken 이 비었습니다.");
            return;
        }

        // 해당 List 로 푸시 알림 보내기
        String title = "신선하지 않은 친구들은 안녕\uD83D\uDC4B";
        String body = "소비 기간이 지난 상품이 있어요. 냉장고를 정리 주세요!";
        MulticastMessage message = makeNotificationMessage(deviceTokenList, title, body);

        BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
        log.info("총 " + response.getSuccessCount() + " 개의 메시지 전송 성공");
        log.info("총 " + response.getFailureCount() + " 개의 메시지 전송 실패");

        if (response.getFailureCount() > 0) {
            checkFailedNotificationResponse(deviceTokenList, response);
        }

        saveItemExpirationNotification(storageItemsAfterExpirationDate);
    }

    private List<String> getDeviceTokenListFromStorageItems(List<StorageItem> storageItemList) {
        return storageItemList.stream()
                .map(StorageItem::getStorageBox)
                .map(StorageBox::getStorage)
                .map(Storage::getMember)
                .map(member -> fcmTokenRepository.getFCMToken(member.getEmail()))
                .distinct().toList();
    }

    private MulticastMessage makeNotificationMessage(List<String> deviceTokenList, String title, String body) {
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .addAllTokens(deviceTokenList)
                .build();
        return message;
    }

    private void checkFailedNotificationResponse(List<String> deviceTokenList, BatchResponse response) {
        List<SendResponse> responses = response.getResponses();
        List<String> failedTokens = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            if (!responses.get(i).isSuccessful()) {
                // The order of responses corresponds to the order of the registration tokens.
                failedTokens.add(deviceTokenList.get(i));
            }
        }
        log.error("List of tokens that caused failures: " + failedTokens);
    }

    public void saveItemExpirationNotification(List<StorageItem> storageItemList) {
        List<StorageNotification> storageNotificationList = storageItemList.stream()
                .map(StorageNotification::new)
                .collect(Collectors.toList());
        notificationRepository.saveAllAndFlush(storageNotificationList);
    }
}
