package com.challenger.fridge.repository;

import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.domain.notification.StorageNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select sn from StorageNotification sn"
            + " join fetch sn.storageItem si"
            + " join fetch si.item i"
            + " join fetch si.storageBox sb"
            + " join fetch sb.storage s"
            + " where sn.member.email = :email")
    List<StorageNotification> findStorageNotificationByEmail(@Param("email") String email);
}
