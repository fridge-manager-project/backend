package com.challenger.fridge.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @NotBlank
    private String deviceToken;
    private String title;
    private String body;

}
