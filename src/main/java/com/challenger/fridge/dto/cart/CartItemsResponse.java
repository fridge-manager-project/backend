package com.challenger.fridge.dto.cart;

import com.challenger.fridge.domain.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemsResponse {

    private Long cartItemId;
    private Long itemId;
    private String itemName;

//    private String subCategoryName;
//    private String mainCategoryName;

    public CartItemsResponse(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.cartItemId = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getItemName();
    }
}
