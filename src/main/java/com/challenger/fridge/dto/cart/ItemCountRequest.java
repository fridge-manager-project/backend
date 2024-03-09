package com.challenger.fridge.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemCountRequest {

    @NotNull(message = "상품 수량을 입력해주세요")
    @Min(value = 1, message = "상품 수량은 1개 이상이어야 합니다.")
    private Long itemCount;
}
