package com.challenger.fridge.controller;

import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.box.response.StorageBoxResponse;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.item.response.StorageItemResponse;
import com.challenger.fridge.exception.StorageItemNotFoundException;
import com.challenger.fridge.repository.StorageItemRepository;
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
    private final StorageItemRepository storageItemRepository;

    @PostMapping("/{storageBoxId}/items/new")
    @Operation(summary = "(검색 탭에서)세부 보관소에 상품 추가", description = "세부 보관소에 상품을 추가한다.(검색 탭에서 냉장고로 바로 추가)")
    public ApiResponse addStorageItemToStorageBox(@RequestBody StorageItemRequest storageItemRequest, @PathVariable("storageBoxId") Long storageBoxId) {
        storageBoxService.saveStorageItem(storageItemRequest, storageBoxId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{storageBoxId}")
    @Operation(summary = "세부 보관소 상품 카테고리별 단건 조회(필터)", description = "세부 보관소 정보와 상품들을 카테고리 별로 조회(url에 파라미터를 보내면 필터 기능) 파라미터가 없다면 그냥 단건 조회")
    public ApiResponse getStorageBox(@PathVariable Long storageBoxId,
                                     @RequestParam(name = "categoryName", required = false) List<String> categoryNames) {
        StorageBoxResponse storageBoxResponse = storageBoxService.findStorageBox(categoryNames, storageBoxId);
        return ApiResponse.success(storageBoxResponse);

    }

    @GetMapping("/items/{storageItemId}")
    @Operation(summary = "세부 보관소에 있는 상품 단건 조회", description = "세부 보관소에 있는 상품을 단건으로 조회한다. (상품 상세 정보)")
    public ApiResponse getStorageBox(@PathVariable Long storageItemId) {
        //단건 조회 로직인데 구지 StorageBoxRepository에서 찾지 않았다 storageBox에서 찾을 수 있지만 Repository에서 메소드를 작성해야 한다
        //결국에는 단건 조회이기 때문에 양방향에서 찾는다해서 큰 장점을 느낄 수 가 없는거 같다.
        //또한 세부 보관소에서 storageItemId을 찾는건 결국 storageItemId만 필요하다.
        //구지 (컨트롤러 -> 서비스 로직 -> 레포지토리)가 아니라 (컨트롤러 -> 레포지토리 방향으로 가는 것이 나은 것 같다)
        StorageItem storageItem = storageItemRepository.findById(storageItemId).orElseThrow(() -> new StorageItemNotFoundException("해당하는 상품이 세부 보관소에 없습니다."));
        return ApiResponse.success(new StorageItemResponse(storageItem));
    }


}
