package com.challenger.fridge.domain;

import com.challenger.fridge.common.MemberRole;
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
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    public List<Storage> storageList=new ArrayList<>();

    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .role(MemberRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
