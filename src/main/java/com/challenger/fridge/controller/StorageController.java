package com.challenger.fridge.controller;


import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.dto.storage.response.StorageItemDetailsResponse;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.challenger.fridge.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Tag(name = "stoarge", description = "storage API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @PostMapping
    @Operation(summary = "보관소 추가", description = "보관소의 이름과 보관방식을 받고 보관소를 추가합니다.")
    public ApiResponse createStorage(@Valid @RequestBody StorageRequest storageRequest
            , BindingResult bindingResult
            , @AuthenticationPrincipal User user
    ) {
        String userEmail = user.getUsername();
        if (userEmail != null) //로그인 되어져 있으면
        {
            storageService.saveStorage(storageRequest, userEmail);
        }
        if (bindingResult.hasErrors()) {
            return ApiResponse.fail(bindingResult.getFieldError().getDefaultMessage());

        }
        return ApiResponse.success(null);
    }

    @GetMapping("/{storageId}")
    @Operation(summary = "보관소 단건 냉장고 조회", description = "보관소 안에서 냉장고에 있는 상품을 조회한다.")
    public ApiResponse getStorageItemList(@PathVariable("storageId") Long storageId) {
        StorageResponse storageItemLists = storageService.findStorageItemLists(storageId);
        return ApiResponse.success(storageItemLists);
    }

    @DeleteMapping("/{storageId}/items/{storageItemId}")
    @Operation(summary = "보관소에 있는 상품 단건 삭제", description = "보관소에 있는 상품을 단건으로 삭제한다.")
    public ApiResponse deleteStorageItem(@PathVariable("storageId") Long storageId
            , @PathVariable("storageItemId") Long storageItemId) {
        storageService.deleteStorageItem(storageItemId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{storageId}/items/{storageItemId}")
    @Operation(summary = "보관소에 있는 상품 단건 조회", description = "보관소에 있는 상품을 단건으로 조회한다")
    public ApiResponse getStorageItem(@PathVariable("storageId") Long storageId
            , @PathVariable("storageItemId") Long storageItemId) {
        StorageItemDetailsResponse storageItemDetailsResponse = storageService.findStorageItemV2(storageId, storageItemId);
        return ApiResponse.success(storageItemDetailsResponse);
    }

    @PostMapping("/{storageId}/items/new")
    @Operation(summary = "검색 탭에서 냉장고로 추가", description = "검색 탭에서 냉장고로 상품을 추가한다.")
    public ApiResponse saveSearchStorageItem(@PathVariable("storageId") Long storageId
            , @RequestBody StorageItemRequest storageItemRequest) {
        storageService.saveStorageItem(storageItemRequest, storageId);
        return ApiResponse.success(null);
    }
}
