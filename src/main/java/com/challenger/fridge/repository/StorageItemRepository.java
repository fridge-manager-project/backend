package com.challenger.fridge.repository;

import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select si from StorageItem si"
            + " join fetch si.storageBox sb"
            + " join fetch sb.storage s"
            + " join fetch s.member m"
            + " where m.allowNotification = true and si.expirationDate < :date")
    List<StorageItem> findStorageItemsByExpirationDateBefore(@Param("date") LocalDate date);

    @Query("select si from StorageItem si where si.storageBox in :storageBoxList")
    List<StorageItem> findStorageItemsByStorageBoxIn(@Param("storageBoxList") List<StorageBox> storageBoxList);

    @Modifying(flushAutomatically = true)
    @Query("delete from StorageItem si where si in :storageItemList")
    void deleteAllInList(@Param("storageItemList") List<StorageItem> storageItemList);
}
