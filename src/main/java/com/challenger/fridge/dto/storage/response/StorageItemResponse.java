package com.challenger.fridge.dto.storage.response;


import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.domain.StorageItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StorageItemResponse {
    private Long storageItemId;
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private LocalDateTime expirationDate;

    public StorageItemResponse(StorageItem storageItem) {
        this.storageItemId = storageItem.getId();
        this.itemId = storageItem.getItem().getId();
        this.itemName = storageItem.getItem().getItemName();
        this.itemCount = storageItem.getQuantity();
        this.expirationDate = storageItem.getExpirationDate();
    }
}
