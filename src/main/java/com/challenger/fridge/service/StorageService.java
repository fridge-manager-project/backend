package com.challenger.fridge.service;


import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageBoxUpdateRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.challenger.fridge.exception.*;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;
    private final StorageBoxRepository storageBoxRepository;

    @Transactional
    public Long saveStorage(StorageSaveRequest storageSaveRequest, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UserEmailNotFoundException("해당 이메일을 가진 사용자가 없습니다."));
        //anyMatch :최소한 한개의 요소가 주어진 조건에 만족하는가 (member가 가지고 있는 storage 중에 storageName이 중복되는게 있는가)
        if (member.getStorageList().stream().anyMatch(storage -> storage.getName().equals(storageSaveRequest.getStorageName()))) {
            throw new StorageNameDuplicateException("보관소의 이름이 중복되었습니다.");
        }
        List<StorageBox> storageBoxList = StorageBox.createStorageBox(storageSaveRequest);
        Storage storage = Storage.createStorage(storageSaveRequest.getStorageName(), storageBoxList, member);
        Storage savedStorage = storageRepository.save(storage);
        return savedStorage.getId();
    }

    @Transactional
    public Long saveStorageBox(StorageBoxSaveRequest storageBoxSaveRequest, Long storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("해당 보관소를 찾을 수 없습니다."));
        StorageMethod storageMethod = storageBoxSaveRequest.getStorageMethod();
        String storageBoxName = storageBoxSaveRequest.getStorageBoxName();
        //만약에 보관소안에 있는 세부 보관소들의 이름들 중 하나라도 중복되는 것이 있다면 예외를 던짐
        if (storage.getStorageBoxList().stream().anyMatch(storageBox -> storageBox.getName().equals(storageBoxName))) {
            throw new StorageBoxNameDuplicateException(storageBoxName + " 은 이미 존재합니다.");
        }
        //여기서 해당 보관소에 대해서 세부 보관소 보관방식 별 개수 확인 로직 호출
        storage.checkStorageBoxCount(storageMethod);
        StorageBox storageBox = StorageBox.createStorageBox(storageBoxName, storageMethod, storage);
        StorageBox savedstorageBox = storageBoxRepository.save(storageBox);
        return savedstorageBox.getId();
    }

    public List<StorageResponse> findStorageList(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UserEmailNotFoundException("해당하는 회원이 없습니다."));
        List<Storage> storageListByMember = storageRepository.findStorageListByMember(member);
        return storageListByMember.stream().map(storage -> new StorageResponse(storage))
                .collect(Collectors.toList());

    }

    public StorageResponse findStorage(Long storageId) {
        Storage storage = storageRepository.findStorageById(storageId).orElseThrow(() -> new StorageNotFoundException("해당 하는 보관소가 없습니다."));
        return new StorageResponse(storage);
    }

    @Transactional
    public void updateStorageBox(StorageBoxUpdateRequest storageBoxUpdateRequest, Long storageBoxId, Long storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("해당하는 보관소가 없습니다."));
        if (storage.getStorageBoxList().stream().anyMatch(storageBox -> storageBox.getName().equals(storageBoxUpdateRequest.getStorageBoxName()))) {
            throw new StorageBoxNameDuplicateException(storageBoxUpdateRequest.getStorageBoxName() + " 은 이미 존재합니다.");
        }
        StorageBox storageBox = storageBoxRepository.findById(storageBoxId).orElseThrow(() -> new StorageBoxNotFoundException("해당하는 세부 보관소가 없습니다."));
        storageBox.changeStorageBox(storageBoxUpdateRequest);
    }

    @Transactional
    public void deleteStorageBox(Long storageBoxId, Long storageId) {
        StorageBox storageBox = storageBoxRepository.findById(storageBoxId).orElseThrow(() -> new StorageBoxNotFoundException("해당하는 세부 보관소가 없습니다."));
        storageBoxRepository.delete(storageBox);
    }

}
