package com.challenger.fridge.dto.storage.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "보관소 추가 Request")
public class StorageSaveRequest {
    @Schema(description = "보관소의 이름")
    @NotEmpty(message = "보관소의 이름을 필수로 입력하세요")
    private String storageName;

    @Schema(description = "냉장고 개수")
    @NotNull
    @Max(value = 10, message = "냉장고 개수는 10개 이하여야 합니다")
    private Long fridgeCount;

    @Schema(description = "냉동고 개수")
    @NotNull
    @Max(value = 10, message = "냉동고 개수는 10개 이하여야 합니다")
    private Long freezeCount;

}
