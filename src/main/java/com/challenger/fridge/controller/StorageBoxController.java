package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.box.response.StorageBoxResponse;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.service.StorageBoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storagebox")
@Tag(name = "storagebox", description = "세부보관소 API")
public class StorageBoxController {
    private final StorageBoxService storageBoxService;

    @PostMapping("/{storageBoxId}/items/new")
    @Operation(summary = "(검색 탭에서)세부 보관소에 상품 추가", description = "세부 보관소에 상품을 추가한다.(검색 탭에서 냉장고로 바로 추가)")
    public ApiResponse addStorageItemToStorageBox(@RequestBody StorageItemRequest storageItemRequest, @PathVariable("storageBoxId") Long storageBoxId) {
        storageBoxService.saveStorageItem(storageItemRequest, storageBoxId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{storageBoxId}")
    @Operation(summary = "세부 보관소 상품 카테고리별 단건 조회(필터)", description = "세부 보관소 정보와 상품들을 카테고리 별로 조회(url에 파라미터를 보내면 필터 기능) 파라미터가 없다면 그냥 단건 조회")
    public ApiResponse getStorageBox(@PathVariable Long storageBoxId,
                                     @RequestParam(name = "categoryName",required = false) List<String> categoryNames) {
        StorageBoxResponse storageBoxResponse = storageBoxService.findStorageBox(categoryNames, storageBoxId);
        return ApiResponse.success(storageBoxResponse);

    }


}
