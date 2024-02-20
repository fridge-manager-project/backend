package com.challenger.fridge.dto.cart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemToStorageRequest {
    private Long boxId;
    private List<CartItemRequest> cartItemRequests;

    @Data
    @AllArgsConstructor
    public static class CartItemRequest {
        private Long cartItemId;
        private Long count;
    }
}
