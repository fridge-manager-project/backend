package com.challenger.fridge.dto.item.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@Schema(description = "보관 아이템을 나타내는 DTO")
public class StorageItemRequest {
    @Schema(description = "아이템의 고유 식별자")
    private Long itemId;

    @Schema(description = "아이템의 이름")
    private String itemName;

    @Schema(description = "아이템의 개수")
    private Long itemCount;

    @Schema(description = "아이템의 설명")
    private String itemDescription;

    @Schema(description = "아이템의 유통 기한")
    private String expirationDate;

    @Schema(description = "아이템의 구매 일자")
    private String purchaseDate;

    @Hidden
    public LocalDate getExpireDateAsLocalDate() {
        return LocalDate.parse(expirationDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Hidden
    public LocalDate getPurchaseDateAsLocalDate() {
        return  LocalDate.parse(purchaseDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

