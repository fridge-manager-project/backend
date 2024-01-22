package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입 및 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class SignController {

    private final SignService signService;

    @Operation(summary = "회원 이메일 중복 체크")
    @GetMapping("/sign-up")
    public ApiResponse checkEmail(@RequestParam String email) {
        log.info("email={}",email);
//        return ApiResponse.success(signService.checkDuplicateEmail(email));
        boolean result = signService.checkDuplicateEmail(email);
        if (result) {
            return ApiResponse.error("사용중인 이메일입니다. 다시 입력해주세요");
        } else {
            return ApiResponse.success(null);
        }
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
