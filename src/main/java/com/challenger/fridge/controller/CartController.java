package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse> cartItemList(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        CartResponse cartResponse = cartService.findItems(email);
        return ResponseEntity.ok(ApiResponse.success(cartResponse));
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse> addItemInCart(@PathVariable Long itemId, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        cartService.addItem(email, itemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAllItemsInCart(@AuthenticationPrincipal User user) {
        cartService.deleteAllItemsInCart(user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
