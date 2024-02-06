package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public boolean existsByEmail(String email);

    public Optional<Member> findByEmail(String email);

}
