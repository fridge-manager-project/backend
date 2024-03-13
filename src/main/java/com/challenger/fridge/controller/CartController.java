package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.cart.CartItemMoveRequest;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.dto.cart.ItemCountRequest;
import com.challenger.fridge.service.CartService;
import com.challenger.fridge.service.CartStorageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartStorageService cartStorageService;

    @Operation(summary = "장바구니 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse> cartItemList(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        CartResponse cartResponse = cartService.findItems(email);
        return ResponseEntity.ok(ApiResponse.success(cartResponse));
    }

    @Operation(summary = "장바구니에 상품 담기 API")
    @PostMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse> addItemInCart(@PathVariable Long itemId, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        cartService.addItem(email, itemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "장바구니의 모든 상품 비우기 API")
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAllItemsInCart(@AuthenticationPrincipal User user) {
        cartService.deleteAllItemsInCart(user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "장바구니 상품 단건 삭제 API")
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteItemInCart(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "장바구니 상품을 냉장고로 이동 API")
    @PostMapping("/items")
    public ResponseEntity<ApiResponse> moveItemToStorage(@RequestBody CartItemMoveRequest cartItemMoveRequest) {
        cartStorageService.moveItems(cartItemMoveRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "장바구니 상품 수량 조절 API")
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> changeCartItemCount(@PathVariable Long cartItemId, @Valid @RequestBody ItemCountRequest itemCountRequest) {
        cartService.changeItemCount(cartItemId, itemCountRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
