package com.challenger.fridge.dto.box.request;


import com.challenger.fridge.exception.StorageMethodMatchingException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.web.bind.MethodArgumentNotValidException;

public enum StorageMethod {
    FRIDGE, FREEZE;

    @JsonCreator
    public static StorageMethod from(String strMethod) {
        for (StorageMethod enumMethod : StorageMethod.values()) {
            if (enumMethod.name().equalsIgnoreCase(strMethod)) {
                return enumMethod;
            }
        }
        // 만약에 해당하는 값들이 없을 경우에는 null처리를 한다
        // 유효성 검사에서 @NotNull에 걸리기 때문에 예외 메시지와 예외 처리 가능
        return null;
    }
}
