package com.challenger.fridge.service;


import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageNameDuplicateException;
import com.challenger.fridge.exception.UserEmailNotFoundException;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveStorage(StorageSaveRequest storageSaveRequest, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UserEmailNotFoundException("해당 이메일을 가진 사용자가 없습니다."));
        //anyMatch :최소한 한개의 요소가 주어진 조건에 만족하는가 (member가 가지고 있는 storage 중에 storageName이 중복되는게 있는가)
        if(member.getStorageList().stream().anyMatch(storage -> storage.getName().equals(storageSaveRequest.getStorageName())))
        {
            throw new StorageNameDuplicateException("보관소의 이름이 중복되었습니다.");
        }
        List<StorageBox> storageBoxList = StorageBox.createStorageBox(storageSaveRequest);
        Storage storage = Storage.createStorage(storageSaveRequest.getStorageName(), storageBoxList,member);
        Storage savedStorage = storageRepository.save(storage);
        return savedStorage.getId();
    }

}
