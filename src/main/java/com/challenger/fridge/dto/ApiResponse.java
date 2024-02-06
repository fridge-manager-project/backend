package com.challenger.fridge.dto;

import com.challenger.fridge.common.ApiStatus;

public record ApiResponse(
        ApiStatus status,
        String message,
        Object data
) {
    public static ApiResponse success(Object data) {
        return new ApiResponse(ApiStatus.SUCCESS, null, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(ApiStatus.ERROR, message, null);
    }

    public static ApiResponse fail(String message) {
        return new ApiResponse(ApiStatus.FAIL, message, null);
    }

}
