package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.dto.cart.CartItemResponse;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.dto.cart.ItemCountRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
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
        signService.registerMember(new SignUpRequest(memberWithoutItems, "1234", memberNameWithoutItems));

        Long firstCartItemId = cartService.addItem(memberWithThreeItems, 1L);
        Long secondCartItemId = cartService.addItem(memberWithThreeItems, 2L);
        Long thirdCartItemId = cartService.addItem(memberWithThreeItems, 3L);

        CartItem firstCartItem = cartItemRepository.findById(firstCartItemId)
                .orElseThrow(IllegalArgumentException::new);
        CartItem thirdCartItem = cartItemRepository.findById(thirdCartItemId)
                .orElseThrow(IllegalArgumentException::new);
        firstCartItem.changePurchase();
        thirdCartItem.changePurchase();
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
        assertEquals(cartItem.getItemCount(), 1L);
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
        List<CartItemResponse> itemsResponses = cartResponse.getCartItems();

        assertEquals(3, cartResponse.getCount());

        assertEquals(1, itemsResponses.get(0).getItemId());
        assertEquals(2, itemsResponses.get(1).getItemId());
        assertEquals(3, itemsResponses.get(2).getItemId());

        assertThat(itemsResponses.get(0).getIsPurchased()).isTrue();
        assertThat(itemsResponses.get(1).getIsPurchased()).isFalse();
        assertThat(itemsResponses.get(2).getIsPurchased()).isTrue();
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

    @DisplayName("장바구니에 담긴 상품 수량 조절")
    @Test
    void changeItemCount() {
        String emailWithThreeItems = memberWithThreeItems;
        Long cartItemId = cartService.addItem(emailWithThreeItems, 4L);
        ItemCountRequest itemCountRequest = new ItemCountRequest(5L);

        Long fiveCartItemId = cartService.changeItemCount(cartItemId, itemCountRequest);
        CartItem cartItem = cartItemRepository.findById(fiveCartItemId)
                .orElseThrow(IllegalArgumentException::new);
        
        assertThat(cartItem.getItemCount()).isEqualTo(5L);
    }

    @DisplayName("장바구니에 담긴 상품 구매 상태 변경")
    @Test
    void changeItemPurchase() {
        String emailWithThreeItems = memberWithThreeItems;
        Long purchasedCartItemId = cartService.addItem(emailWithThreeItems, 4L);
        Long cartItemId = cartService.addItem(emailWithThreeItems, 5L);

        cartService.changeItemPurchase(purchasedCartItemId);
        CartItem purchasedCartItem = cartItemRepository.findById(purchasedCartItemId)
                .orElseThrow(IllegalArgumentException::new);
        CartItem CartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(purchasedCartItem.getIsPurchased()).isTrue();
        assertThat(CartItem.getIsPurchased()).isFalse();
    }
}