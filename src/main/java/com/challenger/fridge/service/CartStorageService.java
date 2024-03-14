package com.challenger.fridge.service;

import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.cart.CartItemRequest;
import com.challenger.fridge.dto.cart.CartItemMoveRequest;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartStorageService {

    private final CartItemRepository cartItemRepository;
    private final StorageBoxRepository storageBoxRepository;
    private final StorageItemRepository storageItemRepository;

    @Transactional
    public void moveItems(CartItemMoveRequest cartItemMoveRequest, String email) {
        StorageBox storageBox = storageBoxRepository.findById(cartItemMoveRequest.getBoxId())
                .orElseThrow(() -> new IllegalArgumentException("세부 보관소를 찾을 수 없습니다."));

        // cartItem 중 isPurchased 된 데이터만 구하기
        List<CartItem> cartItemList = cartItemRepository.findPurchasedItemByEmail(email);
        List<StorageItem> storageItemList = cartItemList.stream()
                .map(cartItem -> new StorageItem(cartItem.getItemCount(), cartItem.getItem(), storageBox))
                .collect(Collectors.toList());

        // bulk insert
        storageItemRepository.saveAll(storageItemList);

        // cartItem 삭제
        cartItemRepository.deleteMovedItemInBatch(cartItemList);
    }
}
