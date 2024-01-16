package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class SignController {

    private final SignService signService;

    @GetMapping("/sign-up")
    public ApiResponse checkEmail(@RequestBody String email) {
        return ApiResponse.success(signService.checkDuplicateEmail(email));
    }

    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }
}
