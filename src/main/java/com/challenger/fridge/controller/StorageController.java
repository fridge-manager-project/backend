package com.challenger.fridge.controller;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.StorageItemResponse;
import com.challenger.fridge.dto.StorageRequest;
import com.challenger.fridge.dto.StorageResponse;
import com.challenger.fridge.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "stoarge", description = "storage API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @PostMapping
    @Operation(summary = "보관소 추가", description = "보관소의 이름과 보관방식을 받고 보관소를 추가합니다.")
    public ApiResponse createStorage(
            @RequestBody StorageRequest storageRequest
            ,@AuthenticationPrincipal User user) {
        String userEmail = user.getUsername();
        if (userEmail != null) //로그인 되어져 있으면
        {
            storageService.saveStorage(storageRequest, userEmail);
        }
        return ApiResponse.success(null);
    }

    @GetMapping("/{storageId}")
    @Operation(summary = "보관소 단건 냉장고 조회", description = "보관소 안에서 냉장고에 있는 상품을 조회한다.")
    public ApiResponse getStorageItemList(@PathVariable("storageId") Long storageId)
    {
        StorageResponse storageItemLists = storageService.findStorageItemLists(storageId);
        return ApiResponse.success(storageItemLists);
    }




}
