package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.member.MemberInfoResponse;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

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
                                      @RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changeUserInfo(user.getUsername(), changePasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
