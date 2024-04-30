package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    @Query("select s from Storage s left join fetch s.storageBoxList where s.member=:member")
    List<Storage> findStorageListByMember(@Param("member") Member member);

    @Query("select s from Storage s left join fetch s.storageBoxList where s.id=:storageId")
    Optional<Storage> findStorageById(@Param("storageId") Long storageId);

    @Modifying(flushAutomatically = true)
    @Query("delete from Storage s where s in :storageList")
    void deleteAllInList(@Param("storageList") List<Storage> storageList);
}
