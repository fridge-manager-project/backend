package com.challenger.fridge.dto.notification;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.domain.notification.StorageNotification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageNotificationResponse {
    private Long notificationId;
    private String itemName;
    private Long storageId;
    private String storageName;
    private Long storageBoxId;
    private String storageBoxName;
    private LocalDate itemExpiration;
    private Boolean isRead;
    private LocalDateTime createdDate;

    public static StorageNotificationResponse from(StorageNotification storageNotification) {
        StorageItem storageItem = storageNotification.getStorageItem();
        StorageBox storageBox = storageItem.getStorageBox();
        Storage storage = storageBox.getStorage();

        return StorageNotificationResponse.builder()
                .notificationId(storageNotification.getId())
                .itemName(storageItem.getItem().getItemName())
                .storageId(storage.getId())
                .storageName(storage.getName())
                .storageBoxId(storageBox.getId())
                .storageBoxName(storageBox.getName())
                .itemExpiration(storageItem.getExpirationDate())
                .isRead(false)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
