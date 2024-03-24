package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.notification.NotificationRequest;
import com.challenger.fridge.service.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FCMController {

    private final FCMService fcmService;

    @Operation(summary = "테스트 알림 보내기")
    @PostMapping("/members/test")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotificationRequest notificationRequest)
            throws FirebaseMessagingException {
        fcmService.sendTestNotification(notificationRequest);
        return ResponseEntity.ok(ApiResponse.success("FCM 테스트 메시지 보내기 성공"));
    }
}
