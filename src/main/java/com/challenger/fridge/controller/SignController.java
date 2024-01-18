package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입 및 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class SignController {

    private final SignService signService;

    @Operation(summary = "회원 이메일 중복 체크")
    @GetMapping("/sign-up")
    public ApiResponse checkEmail(@RequestBody String email) {
        return ApiResponse.success(signService.checkDuplicateEmail(email));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponse signUp(@RequestBody SignInRequest request) {
        return ApiResponse.success(signService.signIn(request));
    }
}
