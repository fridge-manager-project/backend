package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.dto.cart.ItemCountRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Long itemCount;

    private Boolean isPurchased;

    protected CartItem(Cart cart, Item item) {
        this.cart = cart;
        this.item = item;
        this.itemCount = 1L;
        this.isPurchased = false;
    }

    public static CartItem createCartItem(Cart cart, Item item) {
        CartItem cartItem = new CartItem(cart, item);
        cart.getCartItemList().add(cartItem);
        return cartItem;
    }

    public void changeCount(ItemCountRequest itemCountRequest) {
        this.itemCount = itemCountRequest.getItemCount();
    }

    public void changePurchase() {
        this.isPurchased = !this.isPurchased;
    }
}
