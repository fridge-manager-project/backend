package com.challenger.fridge.dto.cart;

import static java.util.stream.Collectors.toList;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long count;
    private Long cartId;
    private List<CartItemsResponse> cartItems;

    public CartResponse(Cart cart, List<CartItem> cartItemList) {
        this.cartId = cart.getId();
        this.count = (long) cartItemList.size();
        this.cartItems = cartItemList.stream()
                .map(CartItemsResponse::new)
                .collect(toList());

    }
}
