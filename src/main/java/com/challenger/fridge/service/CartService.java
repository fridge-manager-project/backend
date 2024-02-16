package com.challenger.fridge.service;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.repository.CartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public CartResponse findItems(String email) {
        List<Cart> cartList = cartRepository.findItemsByEmail(email);
        if(cartList.size() == 0) {
            throw new IllegalArgumentException("장바구니를 찾을 수 없습니다.");
        }
        return new CartResponse(cartList.get(0));
    }
}
