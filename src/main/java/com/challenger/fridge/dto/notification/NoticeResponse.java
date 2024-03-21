package com.challenger.fridge.dto.notification;

import com.challenger.fridge.domain.notification.Notice;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponse {
    private Long notificationId;
    private String title;
    private String body;
    private LocalDate createdDate;

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                .notificationId(notice.getId())
                .title(notice.getTitle())
                .body(notice.getBody())
                .createdDate(notice.getCreatedDate())
                .build();
    }
}
