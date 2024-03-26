package com.challenger.fridge.dto.cart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemMoveRequest {
    private Long boxId;
//    private List<CartItemRequest> cartItemRequests;
}
