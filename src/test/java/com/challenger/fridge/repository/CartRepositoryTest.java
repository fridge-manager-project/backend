package com.challenger.fridge.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.service.SignService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SignService signService;

    @BeforeEach
    void setUp() {
        SignUpRequest signUpRequest = new SignUpRequest("springTest123@test.com", "1234", "test");
        signService.registerMember(signUpRequest);
    }

    @DisplayName("장바구니 조회")
    @Test
    void findCart() {
        String email = "springTest123@test.com";

        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        Member member = cart.getMember();
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(member.getNickname()).isEqualTo(findMember.getNickname());
    }
}