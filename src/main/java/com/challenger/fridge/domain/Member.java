package com.challenger.fridge.domain;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Storage> storageList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Cart cart;
    //해당 회원의 푸쉬 토큰
    private String pushToken;
    //해당 회원의 알림 설정 유무
    private boolean allowNotification;

    public static Member from(SignUpRequest request, PasswordEncoder encoder, Cart cart) {
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(MemberRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .cart(cart)
                .pushToken(null)
                .allowNotification(false)
                .build();
        cart.allocateMember(member);
        return member;
    }

    public void allocateCart(Cart cart) {
        this.cart = cart;
    }

    public void changeMainStorage(Storage storage) {
        storageList.stream()
                .filter(mainStorage -> mainStorage.getStatus() == StorageStatus.MAIN)
                .findFirst()
                .ifPresent(mainStorage -> {
                    // 현재 Main 보관소를 찾았으므로 현재 MAIN을 NORMAL로 바꾼다.
                    mainStorage.changeStorageStatus(StorageStatus.NORMAL);

                });
        // MAIN으로 바꿀 보관소의 Status를 MAIN으로으로 바꾼다
        storage.changeStorageStatus(StorageStatus.MAIN);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, PasswordEncoder encoder) {
        if (!encoder.matches(changePasswordRequest.getCurrentPassword(), password)) {
            throw new IllegalArgumentException("현재 비밀번호와 같지 않습니다. 다시 입력해주세요.");
        }
        this.password = encoder.encode(changePasswordRequest.getNewPassword());
    }
}
