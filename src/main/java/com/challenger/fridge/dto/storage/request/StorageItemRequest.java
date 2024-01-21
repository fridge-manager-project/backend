package com.challenger.fridge.dto.storage.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class StorageItemRequest {
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private String expireDate;
    private String purchaseDate;

    public LocalDateTime getExpireDateAsLocalDateTime() {
        return expireDate != null ? LocalDateTime.parse(expireDate, DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    public LocalDateTime getPurchaseDateAsLocalDateTime() {
        return purchaseDate != null ? LocalDateTime.parse(purchaseDate, DateTimeFormatter.ISO_DATE_TIME) : null;
    }
}
