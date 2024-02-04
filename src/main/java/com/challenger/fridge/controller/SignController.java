package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "회원가입 및 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class SignController {

    private final long COOKIE_EXPIRATION = 7776000; // 90일
    private final SignService signService;

    @Operation(summary = "회원 이메일 중복 체크")
    @GetMapping("/sign-up")
    public ApiResponse checkEmail(@RequestParam String email) {
        log.info("Controller : email={}", email);
        signService.checkDuplicateEmail(email);
        return ApiResponse.success(null);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signUp(@RequestBody SignInRequest request) {
//        return ApiResponse.success(signService.signIn(request));
        SignInResponse response = signService.signIn(request);

        HttpCookie httpCookie = ResponseCookie.from("refresh-token", response.getTokenInfo().getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getTokenInfo().getAccessToken())
                .build();
    }
}
