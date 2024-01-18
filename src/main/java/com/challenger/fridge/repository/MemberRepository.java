package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmail(String email);
    public Optional<Member> findByEmail(String email);
}
