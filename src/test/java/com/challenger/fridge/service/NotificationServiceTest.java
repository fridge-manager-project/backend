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
import com.challenger.fridge.repository.StorageItemRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        String email = "jjw@test.com";
        SignUpRequest signUpRequest = new SignUpRequest(email, "Abcdedf1!", "jjw");
        signService.registerMember(signUpRequest);

        StorageSaveRequest storageSaveRequest = new StorageSaveRequest("테스트 보관소", 1L, 1L);
        storageId = storageService.saveStorage(storageSaveRequest, email);

        StorageBoxSaveRequest storageBoxSaveRequest = new StorageBoxSaveRequest("테스트용 냉장실", StorageMethod.FRIDGE);
        storageBoxId = storageService.saveStorageBox(storageBoxSaveRequest, storageId);

        eatableItemList = em.createQuery("select i from Item i where i.id in (1, 2, 3)", Item.class)
                .getResultList();

        eatableItemList.stream()
                .map(item -> new StorageItemRequest(item.getId(), item.getItemName(), 2L, "테스트용 설명",
                        LocalDate.now().plusDays(1).toString(), LocalDate.now().toString()))
                .forEach(storageItemRequest -> storageBoxService.saveStorageItem(storageItemRequest, storageBoxId));

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

        for (int i = 0; i < storageNotificationResponses.size(); i++) {
            StorageNotificationResponse storageNotificationResponse = storageNotificationResponses.get(i);
            assertThat(storageNotificationResponse.getStorageId()).isEqualTo(storageId);
            assertThat(storageNotificationResponse.getStorageBoxId()).isEqualTo(storageBoxId);
            assertThat(storageNotificationResponse.getItemName()).isEqualTo(eatableItemList.get(i).getItemName());
            assertThat(storageNotificationResponse.getItemExpiration()).isEqualTo(LocalDate.now().plusDays(1));
        }
    }
}