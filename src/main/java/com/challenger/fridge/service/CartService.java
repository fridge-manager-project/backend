package com.challenger.fridge.service;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.dto.cart.ItemCountRequest;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.CartRepository;
import com.challenger.fridge.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    public CartResponse findItems(String email) {
        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));
        List<CartItem> cartItems = cart.getCartItemList().stream()
                .map(cartItem -> cartItemRepository.findItemsById(cartItem.getId())).toList();
        return new CartResponse(cart, cartItems);
    }

    @Transactional
    public Long addItem(String email, Long itemId) {
        // 장바구니를 찾는다
        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        // 이미 있는 상품은 추가하지 않아도 된다. -> "해당 상품은 장바구니에 있습니다." 메시지
        List<CartItem> cartItemList = cart.getCartItemList();
        List<CartItem> duplicateCartItem = cartItemList.stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId)).toList();
        if (!duplicateCartItem.isEmpty()) {
            throw new IllegalArgumentException("해당 상품은 장바구니에 있습니다.");
        }

        // 상품을 찾는다
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. 다시 시도해주세요"));

        // 장바구니에 상품을 추가한다
        CartItem cartItem = CartItem.createCartItem(cart, item);
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    @Transactional
    public void deleteAllItemsInCart(String email) {
        List<CartItem> cartItemList = cartItemRepository.findByEmail(email);
        cartItemRepository.deleteAllInBatch(cartItemList);
    }

    @Transactional
    public void deleteItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public Long changeItemCount(Long cartItemId, ItemCountRequest itemCountRequest) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에서 해당 상품을 찾을 수 없습니다."));
        cartItem.changeCount(itemCountRequest);
        return cartItem.getId();
    }

    @Transactional
    public void changeItemPurchase(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에서 해당 상품을 찾을 수 없습니다."));
        cartItem.changePurchase();
    }
}
