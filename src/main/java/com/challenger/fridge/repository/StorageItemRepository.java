package com.challenger.fridge.repository;

import com.challenger.fridge.domain.StorageItem;
import java.time.LocalDate;
import java.util.List;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageItemRepository extends JpaRepository<StorageItem, Long> {

    @Query("select si from StorageItem si"
            + " join fetch si.storageBox sb"
            + " join fetch sb.storage s"
            + " join fetch s.member m"
            + " where m.allowNotification = true and si.expirationDate between :startDate and :endDate")
    List<StorageItem> findStorageItemsByExpirationDateBetween(@Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate);

    List<StorageItem> findStorageItemsByExpirationDateAfter(LocalDate date);
}
