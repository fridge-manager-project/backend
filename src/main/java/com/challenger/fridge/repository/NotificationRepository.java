package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
