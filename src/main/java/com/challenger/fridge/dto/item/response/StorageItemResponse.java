package com.challenger.fridge.dto.item.response;

import com.challenger.fridge.domain.StorageItem;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "세부 보관소 StorageItem 정보")
public class StorageItemResponse {
    @Schema(description = "세부 보관소 상품 고유 ID")
    private Long storageItemId;
    @Schema(description = "상품 고유 ID")
    private Long itemId;
    @Schema(description = "상품 이름")
    private String itemName;
    @Schema(description = "상품 개수")
    private Long itemCount;
    @Schema(description = "상품 설명")
    private String itemDescription;
    @Schema(description = "유통 기한")
    private LocalDate expirationDate;

    public StorageItemResponse(StorageItem storageItem) {
        this.storageItemId = storageItem.getId();
        this.itemId = storageItem.getItem().getId();
        this.itemName = storageItem.getItem().getItemName();
        this.itemCount = storageItem.getQuantity();
        this.itemDescription = storageItem.getItemDescription();
        this.expirationDate=storageItem.getExpirationDate();
    }



}
