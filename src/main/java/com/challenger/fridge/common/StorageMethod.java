package com.challenger.fridge.common;

import com.challenger.fridge.exception.StorageMethodMachingException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum StorageMethod {
    FRIDGE, FREEZER, ROOM;

    /**
     *  json에서 String 값으로 넘어온 값을 enum타입으로 바꿔준다(현석)
     * @param strMethod
     * @return StorageMethod
     */
    @JsonCreator
    public static StorageMethod from(String strMethod)
    {
        for (StorageMethod enumMethod : StorageMethod.values()) {
            if (enumMethod.name().equalsIgnoreCase(strMethod)) {
                return enumMethod;
            }
        }
        throw new StorageMethodMachingException("보관 저장 방식이 잘못되었습니다.");
    }


}
