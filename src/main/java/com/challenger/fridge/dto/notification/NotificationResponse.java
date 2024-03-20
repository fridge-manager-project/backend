package com.challenger.fridge.dto.notification;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private List<StorageNotificationResponse> storageNotificationResponses;
    private List<NoticeResponse> noticeResponses;
}
