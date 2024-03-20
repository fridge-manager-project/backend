package com.challenger.fridge.dto.notification;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponse {
    private Long notificationId;
    private String title;
    private String body;
    private LocalDate createdDate;
}
