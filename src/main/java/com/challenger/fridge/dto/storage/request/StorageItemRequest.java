package com.challenger.fridge.dto.storage.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageItemRequest {
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private String expireDate;
    private String purchaseDate;

    @Hidden
    public LocalDateTime getExpireDateAsLocalDateTime() {
        return expireDate != null ? LocalDateTime.parse(expireDate, DateTimeFormatter.ISO_DATE_TIME) : null;
    }
    @Hidden
    public LocalDateTime getPurchaseDateAsLocalDateTime() {
        return purchaseDate != null ? LocalDateTime.parse(purchaseDate, DateTimeFormatter.ISO_DATE_TIME) : null;
    }
}
