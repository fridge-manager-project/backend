package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
@Tag(name = "storage", description = "보관소 API")
public class StorageController {
    private final StorageService storageService;

    @PostMapping
    @Operation(summary = "보관소 추가", description = "보관소를 추가한다(사용자가 현재 보관소가 하나라도 존재하면 NORMAL로 없으면 회원가입이라고 인식후 MAIN으로 만듬")
    public ApiResponse createStorage(@Valid @RequestBody StorageSaveRequest storageSaveRequest
            , BindingResult bindingResult
            , @AuthenticationPrincipal User user) {
        String userEmail = user.getUsername();
        if (bindingResult.hasErrors()) {
            return ApiResponse.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        storageService.saveStorage(storageSaveRequest, userEmail);


        return ApiResponse.success(null);
    }


}
