package com.challenger.fridge.domain;

import com.challenger.fridge.common.MemberType;
import com.challenger.fridge.dto.sign.SignUpRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    private LocalDateTime createdAt;

    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .type(MemberType.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
