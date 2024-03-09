package com.challenger.fridge.repository;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional(readOnly = false)
class StorageRepositoryTest {
    @Autowired
    private StorageRepository storageRepository;
    @Test
    void testFindStorageListByMember() {
        // 실제 데이터베이스에서 데이터를 가져오는 테스트
        List<Storage> storageList = storageRepository.findStorageListByMember(Member.builder()
                        .id(1L)
                .email("tlatms8619@naver.com")
                .password("1234")
                .nickname("심현석")
                .role(MemberRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build());


            storageList.stream().forEach(storage -> storage.getStorageBoxList().stream()
                    .forEach(storageBox -> System.out.println(storageBox.getDtype())));


    }
}
