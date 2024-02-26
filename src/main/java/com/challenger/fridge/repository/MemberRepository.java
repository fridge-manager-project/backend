package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public boolean existsByEmail(String email);

    public Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("select m from Member m " +
            "join fetch m.storageList s " +
            "join fetch s.storageBoxList sb " +
            "join fetch sb.storageItemList si " +
            "where si.expirationDate >= :startDate and si.expirationDate <= :endDate and m.allowNotification=true ")
    List<Member> findMembersWithExpiringItemsAndNotificationAllow(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select m from Member m " +
            "join fetch m.cart c ")
    List<Member> findMembersAndCarts();


}
