package com.challenger.fridge.dto.notification;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private LocalDate createdDate;
}
