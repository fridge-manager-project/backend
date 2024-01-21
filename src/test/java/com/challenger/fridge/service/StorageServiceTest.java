package com.challenger.fridge.service;

import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.repository.StorageRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class StorageServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    StorageService storageService;
    @Autowired
    StorageRepository storageRepository;

    @Test
    @DisplayName("냉장고 추가 테스트")
    public void 냉장고추가() throws Exception {
        //given
        Member member = Member.builder()
                .email("123")
                .name("123")
                .password("123").build();
        em.persist(member);

        StorageRequest storageRequest = new StorageRequest();
        storageRequest.setStorageMethod(StorageMethod.FRIDGE);
        storageRequest.setStorageName("심현석 냉장고");
        //when
        Storage savedStorage = storageService.saveStorage(storageRequest, member.getEmail());
        em.flush();
        em.clear();
        Storage findStorage = storageRepository.findById(savedStorage.getId()).orElse(null);
        //then
        assertThat(savedStorage.getId()).isEqualTo(findStorage.getId());
    }

    @Test
    @DisplayName("단건냉장고조회")
    public void 단건냉장고조회() {
        Member member = Member.builder()
                .email("123")
                .name("123")
                .password("123").build();
        em.persist(member);
        StorageRequest storageRequest = new StorageRequest();
        storageRequest.setStorageMethod(StorageMethod.FRIDGE);
        storageRequest.setStorageName("심현석 냉장고");
        Storage storage = Storage.createStorage(storageRequest, member);
        em.persist(storage);
    }

}
