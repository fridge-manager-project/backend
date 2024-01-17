package com.challenger.fridge.dto.storage.response;


import com.challenger.fridge.common.StorageMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class StorageItemResponse
{
    private Long storageItemId;
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private LocalDateTime expirationDate;
    public StorageItemResponse(Long storageItemId, Long itemId, String itemName, Long itemCount, LocalDateTime expirationDate) {
        this.storageItemId = storageItemId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.expirationDate = expirationDate;
    }
}
