package com.challenger.fridge.service;

import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ItemRepository itemRepository;

    @DisplayName("장바구니에 없는 상품 추가")
    @Test
    void addItemNotInCart() {
        String email = "bbb@test.com";
        Long itemId = 2L;

        Long cartItemId = cartService.addItem(email, itemId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(IllegalArgumentException::new);
        String itemName = cartItem.getItem().getItemName();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);

        assertEquals(item.getItemName(), itemName);
    }

    @DisplayName("장바구니에 있는 상품 추가시 예외 발생")
    @Test
    void addItemInCart() {
        String email = "bbb@test.com";
        Long itemId = 1L;

        assertThrows(IllegalArgumentException.class,
                () -> cartService.addItem(email, itemId));
    }
}