package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.notification.NotificationResponse;
import com.challenger.fridge.dto.notification.StorageNotificationResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;
    @Autowired
    SignService signService;
    @Autowired
    StorageService storageService;
    @Autowired
    StorageBoxService storageBoxService;
    @Autowired
    FCMService fcmService;
    @Autowired
    StorageItemRepository storageItemRepository;
    @Autowired
    EntityManager em;

    List<Item> eatableItemList;
    Long storageId;
    Long storageBoxId;
    String email = "jjw@test.com";

    @BeforeEach
    void setUp() {
        createTestMember(email);
        createTestStorage();
        createTestStorageBox();

        eatableItemList = createItemListBetweenIds(1L, 3L);
        saveStorageItems(eatableItemList, LocalDate.now().plusDays(1));

        List<StorageItem> storageItemsByExpirationDateBetween = storageItemRepository.findStorageItemsByExpirationDateBetween(
                LocalDate.now(), LocalDate.now().plusDays(3));

        fcmService.saveItemExpirationNotification(storageItemsByExpirationDateBetween);
    }

    @Test
    @DisplayName("모든 보관소 알림 조회")
    void findAllStorageNotification() {
        String email = "jjw@test.com";

        NotificationResponse notificationResponse = notificationService.findAllNotification(email);
        List<StorageNotificationResponse> storageNotificationResponses = notificationResponse.getStorageNotificationResponses();

        assertThat(storageNotificationResponses.size()).isEqualTo(eatableItemList.size());
        for (int i = 0; i < storageNotificationResponses.size(); i++) {
            StorageNotificationResponse storageNotificationResponse = storageNotificationResponses.get(i);
            assertThat(storageNotificationResponse.getStorageId()).isEqualTo(storageId);
            assertThat(storageNotificationResponse.getStorageBoxId()).isEqualTo(storageBoxId);
            assertThat(storageNotificationResponse.getItemName()).isEqualTo(eatableItemList.get(i).getItemName());
            assertThat(storageNotificationResponse.getItemExpiration()).isEqualTo(LocalDate.now().plusDays(1));
        }
    }

    private void createTestMember(String email) {
        SignUpRequest signUpRequest = new SignUpRequest(email, "Abcdedf1!", "jjw");
        signService.registerMember(signUpRequest);
    }

    private void createTestStorage() {
        StorageSaveRequest storageSaveRequest = new StorageSaveRequest("테스트 보관소", 1L, 1L);
        storageId = storageService.saveStorage(storageSaveRequest, email);
    }

    private void createTestStorageBox() {
        StorageBoxSaveRequest storageBoxSaveRequest = new StorageBoxSaveRequest("테스트용 냉장실", StorageMethod.FRIDGE);
        storageBoxId = storageService.saveStorageBox(storageBoxSaveRequest, storageId);
    }

    private List<Item> createItemListBetweenIds(Long firstItemId, Long lastItemId) {
        return em.createQuery("select i from Item i where i.id between :firstItemId and :lastItemId",
                        Item.class)
                .setParameter("firstItemId", firstItemId)
                .setParameter("lastItemId", lastItemId)
                .getResultList();
    }

    private void saveStorageItems(List<Item> itemList, LocalDate expirationDate) {
        itemList.stream()
                .map(item -> new StorageItemRequest(item.getId(), item.getItemName(), 2L, "테스트용 설명",
                        expirationDate.toString(), LocalDate.now().toString()))
                .forEach(storageItemRequest -> storageBoxService.saveStorageItem(storageItemRequest, storageBoxId));
    }




}