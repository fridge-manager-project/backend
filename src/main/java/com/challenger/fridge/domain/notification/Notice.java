package com.challenger.fridge.domain.notification;

import com.challenger.fridge.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue(value = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Notification {
    private String title;
    private String body;

    public Notice(Member member, String title, String body) {
        super(member);
        this.title = title;
        this.body = body;
    }
}
