package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.box.StorageBox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String message;

    private boolean isRead;

    private LocalDate createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Notice(String message, boolean isRead, LocalDate createdDate, Member member) {
        this.message = message;
        this.isRead = isRead;
        this.createdDate = createdDate;
        this.member = member;
    }

    public static Notice createNotice(Member member, String message) {
        Notice notice = new Notice(
                message,
                false,
                LocalDate.now(),
                member
        );
        return notice;
    }

}
