package com.challenger.fridge.repository;

import com.challenger.fridge.domain.notification.Notice;
import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.domain.notification.StorageNotification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select sn from StorageNotification sn"
            + " join fetch sn.storageItem si"
            + " join fetch si.item i"
            + " join fetch si.storageBox sb"
            + " join fetch sb.storage s"
            + " where sn.member.email = :email"
            + " order by sn.createdDate desc")
    List<StorageNotification> findStorageNotificationByEmail(@Param("email") String email);

    @Query("select n from Notice n where n.member.email = :email order by n.createdDate desc")
    List<Notice> findNoticeByEmail(@Param("email") String email);

    @Query("select n from Notification n where n.id = :id and n.member.email = :email")
    Optional<Notification> findNotificationByIdAndMemberEmail(@Param("notificationId") Long id,
                                                              @Param("email") String email);

}
