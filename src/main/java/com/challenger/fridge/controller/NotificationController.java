package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.notification.NotificationResponse;
import com.challenger.fridge.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "전체 알림 조회")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllNotification(@AuthenticationPrincipal User user) {
        NotificationResponse notificationResponse = notificationService.findAllNotification(user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(notificationResponse));
    }
}
