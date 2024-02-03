package com.challenger.fridge.dto.box.response;

import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.item.response.CategoryStorageItemResponse;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "세부 보관소 정보 Response")
public class StorageBoxResponse {
    @Schema(description = "세부 보관소 고유 ID")
    private Long storageBoxId;
    @Schema(description = "세부 보관소 이름")
    private String storageBoxName;
    @Schema(description = "세부 보관소 내에 있는 카테고리별 상품 리스트")
    private List<CategoryStorageItemResponse> categoriesItems;

    public StorageBoxResponse(StorageBox storageBox) {
        this.storageBoxId = storageBox.getId();
        this.storageBoxName = storageBox.getName();
    }
    public StorageBoxResponse(StorageBox storageBox,List<CategoryStorageItemResponse> categoryStorageItemList) {
        this.storageBoxId = storageBox.getId();
        this.storageBoxName = storageBox.getName();
        this.categoriesItems=categoryStorageItemList;

    }
}
