package com.challenger.fridge.dto.storage.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageItemRequest {
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private LocalDateTime expireDate;
    private LocalDateTime purchaseDate;
}
