package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String message;

    private Boolean isRead;

    private LocalDate createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Notification(String message, boolean isRead, LocalDate createdDate, Member member) {
        this.message = message;
        this.isRead = isRead;
        this.createdDate = createdDate;
        this.member = member;
    }

    public static Notification createNotice(Member member, String message) {
        return new Notification(
                message,
                false,
                LocalDate.now(),
                member
        );
    }

}
