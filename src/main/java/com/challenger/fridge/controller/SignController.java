package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.TokenInfo;
import com.challenger.fridge.service.FCMService;
import com.challenger.fridge.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private final FCMService fcmService;

    @Operation(summary = "회원 이메일 중복 체크")
    @GetMapping("/sign-up")
    public ApiResponse checkEmail(@RequestParam String email) {
        log.info("Controller : email={}", email);
        signService.checkDuplicateEmail(email);
        return ApiResponse.success(null);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequest request, @RequestHeader("device_token") String deviceToken) {
        TokenInfo tokenInfo = signService.signIn(request, deviceToken);
        fcmService.saveToken(request, deviceToken);

        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenInfo.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
//                .domain("localhost")
//                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInfo.getAccessToken())
                .body(ApiResponse.success(null));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        log.info("accessTokenInHeader : {}", requestAccessToken);
        log.info("refreshTokenInHeader : {}", requestRefreshToken);
        TokenInfo reissuedTokenDto = signService.reissue(requestAccessToken, requestRefreshToken);

        // 토큰 재발급 성공시 RT 저장
        if(reissuedTokenDto != null) {
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .body(ApiResponse.success(null));
        }

        // RT 탈취 가능성이 있으므로 Cookie 삭제 후 재로그인 유도
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.fail("다시 로그인하세요."));
    }

}
