package com.challenger.fridge.dto.storage.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "보관소 수정 Request")
public class StorageUpdateRequest {
    @Schema(description = "메인으로 바꿀 보관소의 고유 값")
    private Long storageId;
}
