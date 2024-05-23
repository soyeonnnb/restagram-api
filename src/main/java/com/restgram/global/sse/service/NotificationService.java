package com.restgram.global.sse.service;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.user.entity.User;
import com.restgram.global.sse.entity.Notification;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.repository.EmitterRepositoryImpl;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);
    void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data);
    String makeTimeIncludeId(Long userId);
    boolean hasLostData(String lastEventId);
    void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter);
    void sendToClient(SseEmitter emitter, String id, Object data);
    void send(User receiver, NotificationType type, Reservation reservation);
    void send(User receiver, NotificationType type, Coupon coupon);
    void sendList(List<User> receiverList, NotificationType type, Reservation reservation);
    void sendList(List<User> receiverList, NotificationType type, Coupon coupon);
    // 예약 생성/예약 2시간 전/취소
    Notification createNotification(User receiver, NotificationType type, Reservation reservation);
    // 쿠폰 발급 시작
    Notification createNotification(User receiver, NotificationType type, Coupon coupon);

}
