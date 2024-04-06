package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.notification.NotificationResponse;
import com.challenger.fridge.dto.notification.StorageNotificationResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.NotificationRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired NotificationService notificationService;
    @Autowired SignService signService;
    @Autowired StorageService storageService;
    @Autowired StorageBoxService storageBoxService;
    @Autowired FCMService fcmService;
    @Autowired StorageItemRepository storageItemRepository;
    @Autowired NotificationRepository notificationRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    List<Item> eatableItemList;
    List<Item> unEatableItemList;
    Long storageId;
    Long storageBoxId;
    static String EMAIL = "springTest@test.com";

    @BeforeEach
    void setUp() {
        createTestMember(EMAIL);
        createTestStorage();
        createTestStorageBox();

        eatableItemList = createItemListBetweenIds(1L, 3L);
        unEatableItemList = createItemListBetweenIds(5L, 10L);

        saveStorageItems(eatableItemList, LocalDate.now().plusDays(1));
        saveStorageItems(unEatableItemList, LocalDate.now().minusDays(1));

        List<StorageItem> storageItemsByExpirationDateBetween = storageItemRepository.findStorageItemsByExpirationDateBetween(
                LocalDate.now(), LocalDate.now().plusDays(3));
        List<StorageItem> storageItemsByExpirationDateBefore = storageItemRepository.findStorageItemsByExpirationDateBefore(
                LocalDate.now());

        fcmService.saveItemExpirationNotification(storageItemsByExpirationDateBetween);
        fcmService.saveItemExpirationNotification(storageItemsByExpirationDateBefore);
    }

    @Test
    @DisplayName("모든 보관소 알림 조회")
    void findAllStorageNotification() {
        //given
        String email = EMAIL;

        //when
        NotificationResponse notificationResponse = notificationService.findAllNotification(email);
        List<StorageNotificationResponse> storageNotificationResponses = notificationResponse.getStorageNotificationResponses();

        //then
        int eatableItemNotificationSize = eatableItemList.size();
        int unEatableItemNotificationSize = unEatableItemList.size();
        int totalNotificationSize = eatableItemNotificationSize + unEatableItemNotificationSize;

        assertThat(storageNotificationResponses.size()).isEqualTo(totalNotificationSize);

        for (int i = totalNotificationSize - 1; i >= totalNotificationSize - eatableItemNotificationSize; i--) {
            StorageNotificationResponse storageNotificationResponse = storageNotificationResponses.get(i);
            assertThat(storageNotificationResponse.getStorageId()).isEqualTo(storageId);
            assertThat(storageNotificationResponse.getStorageBoxId()).isEqualTo(storageBoxId);
            assertThat(storageNotificationResponse.getItemName()).isEqualTo(eatableItemList.get(totalNotificationSize - i - 1).getItemName());
            assertThat(storageNotificationResponse.getItemExpiration()).isEqualTo(LocalDate.now().plusDays(1));
        }

        for (int i = unEatableItemNotificationSize - 1; i >= 0; i--) {
            StorageNotificationResponse storageNotificationResponse = storageNotificationResponses.get(i);
            assertThat(storageNotificationResponse.getStorageId()).isEqualTo(storageId);
            assertThat(storageNotificationResponse.getStorageBoxId()).isEqualTo(storageBoxId);
            assertThat(storageNotificationResponse.getItemName()).isEqualTo(
                    unEatableItemList.get(unEatableItemNotificationSize - i - 1).getItemName());
            assertThat(storageNotificationResponse.getItemExpiration()).isEqualTo(LocalDate.now().minusDays(1));
        }
    }


    @Test
    @DisplayName("알림 읽기 테스트")
    void readNotification() {
        //given
        String email = EMAIL;
        NotificationResponse notificationResponse = notificationService.findAllNotification(email);
        List<StorageNotificationResponse> storageNotificationResponses = notificationResponse.getStorageNotificationResponses();

        if (storageNotificationResponses.isEmpty()) {
            fail("상품을 보관소에 넣은 후 알림을 생성해주세요");
        }

        Long notificationId = storageNotificationResponses.get(0).getNotificationId();

        //when
        Long readNotificationId = notificationService.readNotificationById(notificationId);
        Notification readNotification = notificationRepository.findById(readNotificationId)
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(readNotification.getIsRead()).isTrue();
    }

    private void createTestMember(String email) {
        SignUpRequest signUpRequest = new SignUpRequest(email, "Abcdedf1!", "springTest");
        signService.registerMember(signUpRequest);

    }

    private void createTestStorage() {
        StorageSaveRequest storageSaveRequest = new StorageSaveRequest("테스트 보관소", 1L, 1L);
        storageId = storageService.saveStorage(storageSaveRequest, EMAIL);
        Member member = memberRepository.findByEmail(EMAIL)
                .orElseThrow(IllegalArgumentException::new);
        member.receiveNotification();
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