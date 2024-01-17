package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findMemberByEmail(String memberEmail);
}
