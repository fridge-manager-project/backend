package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.dto.cart.CartItemsResponse;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartServiceTest {

    String memberWithThreeItems = "ccc@test.com";
    String memberWithoutItems = "ddd@test.com";
    String password = "1234";
    String memberNameWithItems = "ccc";
    String memberNameWithoutItems = "ddd";

    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void beforeEach() {
        signService.registerMember(new SignUpRequest(memberWithThreeItems, password, memberNameWithItems));
        cartService.addItem(memberWithThreeItems, 1L);
        cartService.addItem(memberWithThreeItems, 2L);
        cartService.addItem(memberWithThreeItems, 3L);

        signService.registerMember(new SignUpRequest(memberWithoutItems, "1234", memberNameWithoutItems));
    }

    @DisplayName("장바구니에 없는 상품 추가")
    @Test
    void addItemNotInCart() {
        String email = memberWithThreeItems;
        Long itemId = 4L;

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
        String email = memberWithThreeItems;

        assertThrows(IllegalArgumentException.class,
                () -> cartService.addItem(email, 1L));
        assertThrows(IllegalArgumentException.class,
                () -> cartService.addItem(email, 2L));
        assertThrows(IllegalArgumentException.class,
                () -> cartService.addItem(email, 3L));
    }

    @DisplayName("장바구니 상품 조회 - 상품이 있는 장바구니")
    @Test
    void findCartItems() {
        String emailWithItems = memberWithThreeItems;

        CartResponse cartResponse = cartService.findItems(emailWithItems);
        List<CartItemsResponse> itemsResponses = cartResponse.getCartItems();

        assertEquals(3, cartResponse.getCount());
        assertEquals(1, itemsResponses.get(0).getItemId());
        assertEquals(2, itemsResponses.get(1).getItemId());
        assertEquals(3, itemsResponses.get(2).getItemId());
    }

    @DisplayName("장바구니 상품 조회 - 상품이 없는 장바구니")
    @Test
    void findCartWithoutItems() {
        String emailWithoutItems = memberWithoutItems;

        CartResponse cartResponseWithoutItems = cartService.findItems(emailWithoutItems);

        assertEquals(0, cartResponseWithoutItems.getCount());
    }

    @DisplayName("장바구니 비우기")
    @Test
    void deleteAllItemsInCart() {
        String emailWithThreeItems = memberWithThreeItems;

        cartService.deleteAllItemsInCart(emailWithThreeItems);
        em.clear();

        CartResponse deletedResponse = cartService.findItems(emailWithThreeItems);

        assertThat(deletedResponse.getCount()).isEqualTo(0);
        assertThat(deletedResponse.getCartItems().size()).isEqualTo(0);
    }

    @DisplayName("장바구니에 담긴 상품 단건 삭제")
    @Test
    void deleteSelectedItem() {
        String emailWithItems = memberWithThreeItems;
        List<CartItem> cartItemList = cartItemRepository.findByEmail(emailWithItems);
        Long cartItemId = cartItemList.get(1).getId();

        cartService.deleteItem(cartItemId);

        assertThrows(NoSuchElementException.class,
                () -> cartItemRepository.findById(cartItemId).get());
    }
}