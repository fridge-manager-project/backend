package com.challenger.fridge.service;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final FCMService fcmService;
    private final MemberRepository memberRepository;

    @Scheduled(fixedRate = 10000) // 10초
    public void sendTestNotification() {
        List<Member> memberList = memberRepository.findAll();
        try {
            fcmService.sendTestMessage(memberList);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 17 * * *")    // 매일 17:00에 실행
    public void sendExpirationNotifications() {
        //현재 시간
        LocalDate startDate = LocalDate.now();
        //현재 시간으로 부터 3일 후 
        LocalDate endDate = startDate.plusDays(3);
        //위 기간내에 StorageItem을 가지고 있고 알림을 허용한 모든 Member 조회
        List<Member> memberList = memberRepository.findMembersWithExpiringItemsAndNotificationAllow(startDate, endDate);
        if (memberList.isEmpty()) {
            return; // 그러한 회원이 없다면 메서드 종료;
        }
        //호출
        try {
            fcmService.sendExpirationMessageTo(memberList);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(cron = "0 0 17 * * *")    // 매일 17:00에 실행
    public void sendCartNotifications() {
        List<Member> memberList = memberRepository.findMembersAndCarts();
        List<Member> collect = memberList.stream()
                .filter(member -> member.getCart().getCartItemList().size() != 0)
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return; //없으면 종료
        }
        //호출
        try {
            fcmService.sendCartMessageTo(memberList);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
