package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.member.MemberInfoResponse;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.dto.member.MemberNicknameRequest;
import com.challenger.fridge.service.MemberService;
import com.challenger.fridge.service.MemberWithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberWithdrawService memberWithdrawService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> userInfo(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        MemberInfoResponse userInfo = memberService.findUserInfo(email);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @Operation(summary = "회원 비밀번호 수정")
    @PatchMapping("/info")
    public ResponseEntity<ApiResponse> changeMemberPassword(@AuthenticationPrincipal User user,
                                                            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changeUserInfo(user.getUsername(), changePasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "회원 닉네임 수정")
    @PatchMapping("/info/nickname")
    public ResponseEntity<ApiResponse> changeNickname(@AuthenticationPrincipal User user,
                                                      @Valid @RequestBody MemberNicknameRequest memberNicknameRequest) {
        memberService.changeUserNickname(user.getUsername(), memberNicknameRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "알림 설정 on off")
    @PatchMapping("/info/notification")
    public ResponseEntity<ApiResponse> changeNotificationReception
            (@RequestHeader(name = "device_token", required = false) String deviceToken,
             @AuthenticationPrincipal User user) {
        memberService.changeNotificationReception(deviceToken, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation
    @DeleteMapping
    public ResponseEntity<ApiResponse> withdrawMember(@AuthenticationPrincipal User user) {
        memberWithdrawService.withdrawMember(user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
