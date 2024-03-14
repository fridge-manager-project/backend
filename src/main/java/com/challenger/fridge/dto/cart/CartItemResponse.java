package com.challenger.fridge.dto.cart;

import com.challenger.fridge.domain.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private Long itemCount;
    private Boolean isPurchased;
    private String subCategoryName;

    public CartItemResponse(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.itemId = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getItemName();
        this.itemCount = cartItem.getItemCount();
        this.isPurchased = cartItem.getIsPurchased();
        this.subCategoryName = cartItem.getItem().getCategory().getCategoryName();
    }
}
