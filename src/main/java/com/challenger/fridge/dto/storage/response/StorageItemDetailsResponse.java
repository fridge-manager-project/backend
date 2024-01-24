package com.challenger.fridge.dto.storage.response;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StorageItemDetailsResponse {
    private Long storageId;
    private String storageName;
    private Long storageItemId;
    private Long itemId;
    private String itemName;
    private String categoryName;
    private LocalDateTime expiryDate;
    private Long itemCount;

    public StorageItemDetailsResponse(Long storageId, String storageName, Long storageItemId, Long itemId, String itemName, String categoryName, LocalDateTime expiryDate, Long itemCount) {
        this.storageId = storageId;
        this.storageName = storageName;
        this.storageItemId = storageItemId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.expiryDate = expiryDate;
        this.itemCount = itemCount;
    }
}
