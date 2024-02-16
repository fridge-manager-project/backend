package com.challenger.fridge.dto.cart;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long count;
    private Long cartId;
    private List<CartItemsResponse> cartItems;

    public CartResponse(Cart cart) {
        List<CartItem> cartItemList = cart.getCartItemList();
        this.count = (long) cartItemList.size();
        this.cartId = cart.getId();
        cartItemList.forEach(cartItem -> cartItems.add(new CartItemsResponse(cartItem)));
    }
}
