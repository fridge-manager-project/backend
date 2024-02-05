package com.challenger.fridge.domain;


import com.challenger.fridge.domain.box.Freeze;
import com.challenger.fridge.domain.box.Fridge;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.exception.StorageBoxLimitExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class StorageTest {
    @Spy
    private Storage storageMock;


    @Test
    @DisplayName("냉장고 개수 반환 - 현재 보관소에서 냉장의 개수 반환")
    void 냉장고개수를반환() {
        // given
        Fridge fridge1 = new Fridge("냉장고1");
        Fridge fridge2 = new Fridge("냉장고2");
        Freeze freeze1 = new Freeze("냉동고1"); //일부러 냉동고 추가
        storageMock.addStorageBox(fridge1);
        storageMock.addStorageBox(freeze1);
        storageMock.addStorageBox(fridge2);
        // when
        Long fridgeCount = storageMock.getStorageBoxFridgeCount();
        // then
        assertEquals(2L, fridgeCount);
    }

    @Test
    @DisplayName("냉동고 개수 반환 - 현재 보관소에서 냉동고의 개수 반환")
    void 냉동고개수를반환() {
        // given
        storageMock.addStorageBox(new Fridge("냉장고1"));
        storageMock.addStorageBox(new Freeze("냉동고1"));
        storageMock.addStorageBox(new Freeze("냉동고2"));

        // when
        Long freezeCount = storageMock.getStorageBoxFreezeCount();

        // then
        assertEquals(2L, freezeCount);
    }

    @Test
    @DisplayName("세부 보관소 개수 검사 - 냉장고 개수 초과시 예외 발생")
    void 세부보관소개수검사() {
        // given
        StorageMethod method = StorageMethod.FRIDGE;

        when(storageMock.getStorageBoxFridgeCount()).thenReturn(12L);

        // when & then
        assertThrows(StorageBoxLimitExceededException.class, () -> storageMock.checkStorageBoxCount(method));
    }

}
