package com.restgram.global.sse.repository;

import com.restgram.domain.user.entity.User;
import com.restgram.global.sse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);
}
