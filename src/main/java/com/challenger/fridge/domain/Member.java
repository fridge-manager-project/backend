package com.challenger.fridge.domain;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.common.StorageStatus;
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

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    private List<Storage> storageList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Cart cart;

//    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
//        return Member.builder()
//                .email(request.getEmail())
//                .password(encoder.encode(request.getPassword()))
//                .name(request.getName())
//                .role(MemberRole.ROLE_USER)
//                .createdAt(LocalDateTime.now())
//                .build();
//    }

    public static Member from(SignUpRequest request, PasswordEncoder encoder, Cart cart) {
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .role(MemberRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .cart(cart)
                .storageList(new ArrayList<>())
                .build();
        cart.allocateMember(member);
        return member;
    }

    public Member(SignUpRequest request, PasswordEncoder encoder, Cart cart) {
        this.email = request.getEmail();
        this.password = encoder.encode(request.getPassword());
        this.name = request.getName();
        this.role = MemberRole.ROLE_USER;
        this.createdAt = LocalDateTime.now();
        this.cart = cart;
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

    public void changeInfo(Storage newMainStorage, String encodedPassword) {
        changePassword(encodedPassword);
        changeMainStorage(newMainStorage);
    }

    private void changePassword(String encodedPassword) {
        if (this.password.equals(encodedPassword)) {
            throw new IllegalArgumentException("새로운 비밀번호를 입력하세요");
        }
        this.password = encodedPassword;
    }
}
