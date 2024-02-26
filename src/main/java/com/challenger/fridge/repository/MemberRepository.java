package com.challenger.fridge.repository;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public boolean existsByEmail(String email);

    public Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("select m from Member m join fetch m.storageList s"
            + " where m.email = :email and s.status = :status")
    Optional<Member> findMemberStorageByEmail(@Param("email") String email, @Param("status") StorageStatus status);
}
