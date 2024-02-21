package com.challenger.fridge.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemRequest {
    private Long cartItemId;
    private Long count;
}
