package com.challenger.fridge.dto.storage.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema
public class StorageSaveRequest {
    @NotEmpty(message = "보관소의 이름을 필수로 입력하세요")
    private String storageName;
    @NotNull
    private Long fridgeCount;
    @NotNull
    private Long freezeCount;
    @NotNull
    private Long roomCount;
}
