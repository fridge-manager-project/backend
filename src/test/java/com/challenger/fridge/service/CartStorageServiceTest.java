package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.cart.CartItemRequest;
import com.challenger.fridge.dto.cart.CartItemMoveRequest;
import com.challenger.fridge.dto.cart.CartResponse;
import com.challenger.fridge.dto.cart.ItemCountRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartStorageServiceTest {

    @Autowired SignService signService;
    @Autowired CartService cartService;
    @Autowired StorageService storageService;
    @Autowired EntityManager em;
    @Autowired StorageRepository storageRepository;
    @Autowired StorageBoxRepository storageBoxRepository;
    @Autowired CartStorageService cartStorageService;

    static String EMAIL = "springTest@test.com";
    Long storageId;
    List<Item> itemList = new ArrayList<>();
    List<Long> cartItemIdList;

    @BeforeEach
    void setUp() {
        String email = EMAIL;
        signService.registerMember(new SignUpRequest(EMAIL, "1234", "springTest"));
        StorageSaveRequest storageSaveRequest = new StorageSaveRequest("퍼스트 냉장고", 1L, 3L, 2L);
        storageId = createStorageConfig(email, storageSaveRequest);

        Item pork = findItem("돼지고기");
        Item onion = findItem("양파");
        Item greenOnion = findItem("대파");
        Item garlic = findItem("마늘");
        itemList.addAll(Arrays.asList(pork, onion, greenOnion, garlic));

        cartItemIdList = itemList.stream()
                .map(item -> cartService.addItem(email, item.getId())).toList();
    }

    @DisplayName("장바구니의 모든 구매 상품을 보관소로 옮기기")
    @Test
    void moveItemsToBox() {
        //given
        String email = EMAIL;
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(IllegalArgumentException::new);
        Long boxId = storage.getStorageBoxList().get(1).getId();
        CartItemMoveRequest cartItemMoveRequest = new CartItemMoveRequest(boxId);

        //when
        cartItemIdList.forEach(cartItemId -> cartService.changeItemPurchase(cartItemId));
        System.out.println("=================");
        cartStorageService.moveItems(cartItemMoveRequest, email);
        System.out.println("=================");

        CartResponse cartResponse = cartService.findItems(EMAIL);
        StorageBox storageBox = storageBoxRepository.findStorageItemsById(boxId)
                .orElseThrow(IllegalArgumentException::new);
        List<StorageItem> storageItemList = storageBox.getStorageItemList();

        //then
        assertThat(cartResponse.getCartItems().size()).isEqualTo(0);

        assertThat(storageItemList.get(0).getItem().getItemName()).isEqualTo("돼지고기");
        assertThat(storageItemList.get(1).getItem().getItemName()).isEqualTo("양파");
        assertThat(storageItemList.get(2).getItem().getItemName()).isEqualTo("대파");
        assertThat(storageItemList.get(3).getItem().getItemName()).isEqualTo("마늘");

        assertThat(storageItemList.get(0).getQuantity()).isEqualTo(1);
        assertThat(storageItemList.get(1).getQuantity()).isEqualTo(1);
        assertThat(storageItemList.get(2).getQuantity()).isEqualTo(1);
        assertThat(storageItemList.get(3).getQuantity()).isEqualTo(1);
    }

    @DisplayName("장바구니에서 일부 상품 구매후 보관소로 옮기기")
    @Test
    void moveSelectedItemsToBox() {
        //given
        String email = EMAIL;
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(IllegalArgumentException::new);
        Long boxId = storage.getStorageBoxList().get(1).getId();
        Long onionId = cartItemIdList.get(1);
        Long greenOnionId = cartItemIdList.get(2);
        CartItemMoveRequest cartItemMoveRequest = new CartItemMoveRequest(boxId);

        //when
        cartService.changeItemPurchase(onionId);
        cartService.changeItemPurchase(greenOnionId);
        cartStorageService.moveItems(cartItemMoveRequest, email);

        CartResponse cartResponse = cartService.findItems(EMAIL);
        StorageBox storageBox = storageBoxRepository.findStorageItemsById(boxId)
                .orElseThrow(IllegalArgumentException::new);
        List<StorageItem> storageItemList = storageBox.getStorageItemList();

        //then
        assertThat(cartResponse.getCartItems().size()).isEqualTo(2);

        assertThat(storageItemList.get(0).getItem().getItemName()).isEqualTo("양파");
        assertThat(storageItemList.get(1).getItem().getItemName()).isEqualTo("대파");

        assertThat(storageItemList.get(0).getQuantity()).isEqualTo(1);
        assertThat(storageItemList.get(1).getQuantity()).isEqualTo(1);
    }

    private Item findItem(String itemName) {
        return em.createQuery("select i from Item i where i.itemName  = :itemName", Item.class)
                .setParameter("itemName", itemName)
                .getSingleResult();
    }

    private Long createStorageConfig(String email, StorageSaveRequest storageSaveRequest) {
        return storageService.saveStorage(storageSaveRequest, email);
    }
}