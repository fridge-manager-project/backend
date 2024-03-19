package com.challenger.fridge.service;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.NotificationRequest;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.repository.FCMTokenRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.time.LocalDate;
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
    private final MemberRepository memberRepository;
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

//    @Scheduled(fixedRate = 1800000) // 30 min
    @Scheduled(fixedRate = 120000) // 2 min
    public void sendStorageNotification() throws FirebaseMessagingException {
        log.info("소비기한 임박 상품 푸시 알림 보내기 시작");
        // 유통기한이 임박한 상품을 가진 사용자 찾기
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        List<Member> membersWithExpiringItems = memberRepository.findMembersWithExpiringItemsAndNotificationAllow(
                startDate, endDate);

        // 해당 사용자의 email 을 사용하여 deviceTokenList 만들기
        List<String> deviceTokenList = membersWithExpiringItems.stream()
                .map(Member::getEmail)
                .map(fcmTokenRepository::getFCMToken)
                .collect(Collectors.toList());

        // 해당 List 로 푸시 알림 보내기
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("보관소를 확인해주세요!")
                        .setBody("소비 기한이 임박한 상품이 있습니다. 얼른 먹어주세요!")
                        .build())
                .addAllTokens(deviceTokenList)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
        log.info("총 " + response.getSuccessCount() + " 개의 메시지 전송 성공");
        log.info("총 " + response.getFailureCount() + " 개의 메시지 전송 실패");
    }
}
