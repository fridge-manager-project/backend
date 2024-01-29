package com.challenger.fridge.service;


import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.box.Freeze;
import com.challenger.fridge.domain.box.Fridge;
import com.challenger.fridge.domain.box.Room;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageNameDuplicateException;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveStorage(StorageSaveRequest storageSaveRequest, String userEmail) {
        ArrayList<StorageBox> storageBoxes = new ArrayList<>();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 사용자가 없습니다."));
        if (storageRepository.existsByName(storageSaveRequest.getStorageName())) {
            throw new StorageNameDuplicateException("보관소의 이름이 이미 존재합니다.");
        }
        for (int i = 1; i <= storageSaveRequest.getFridgeCount(); i++) {
            Fridge fridge = Fridge.createFridge("냉장고" + i);
            storageBoxes.add(fridge);
        }
        for (int i = 1; i <= storageSaveRequest.getFreezeCount(); i++) {
            Freeze freeze = Freeze.createFridge("냉동고" + i);
            storageBoxes.add(freeze);
        }
        for (int i = 1; i <= storageSaveRequest.getRoomCount(); i++) {
            Room room = Room.createFridge("실온" + i);
            storageBoxes.add(room);
        }
        Storage storage = Storage.createStorage(storageSaveRequest.getStorageName(), storageBoxes,member);
        Storage savedStorage = storageRepository.save(storage);
        return savedStorage.getId();
    }

}
