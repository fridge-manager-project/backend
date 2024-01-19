package com.challenger.fridge.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("중복 사용자 확인")
    void checkDuplicateEmail() {
        String uniqueEmail = "jjw@test.com";
        String duplicatedEmail = "cjw@test.com";
        Assertions.assertThat(memberRepository.existsByEmail(uniqueEmail)).isTrue();
        Assertions.assertThat(memberRepository.existsByEmail(duplicatedEmail)).isFalse();
    }
}