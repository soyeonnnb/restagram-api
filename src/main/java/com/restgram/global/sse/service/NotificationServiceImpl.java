package com.restgram.global.sse.service;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.user.entity.User;
import com.restgram.global.sse.dto.response.NotificationResponse;
import com.restgram.global.sse.entity.Notification;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.repository.EmitterRepository;
import com.restgram.global.sse.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // SSE 유효시간: 1초(1000밀리초) * 60 * 60 = 1시간
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);

        // 버그 방지
        if (emitterRepository.findAllEmitterStartWithByUserId(userId) != null) {
            emitterRepository.deleteAllEmitterStartWithId(userId);
        }
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); //id가 key, SseEmitter가 value

        //오류 종류별 구독 취소 처리
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); //네트워크 오류
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); //시간 초과
        emitter.onError((e) -> emitterRepository.deleteById(emitterId)); //오류

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    // 단순 알림 전송
    @Override
    public void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data, MediaType.APPLICATION_JSON)
                    .reconnectTime(0));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            emitter.completeWithError(exception);
        }
    }

    //Last-Event-ID의 값을 이용하여 유실된 데이터를 찾는데 필요한 시점을 파악하기 위한 형태
    @Override
    public String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }


    @Override
    public boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    @Override
    public void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, SseEmitter> eventCaches = emitterRepository.findAllEmitterStartWithByUserId(userId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Override
    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data, MediaType.APPLICATION_JSON)
                    .reconnectTime(0)
            );
        } catch(Exception exception) {
            emitterRepository.deleteById(id);
            emitter.completeWithError(exception);
        }
    }

    @Override
    @Transactional
    public void send(User receiver, NotificationType type, Reservation reservation) {

        Notification notification = createNotification(receiver, type, reservation);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(receiver.getId());

        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, NotificationResponse.of(notification));
                }
        );
    }
//
//    @Override
//    @Transactional
//    public void send(User receiver, NotificationType type, Coupon coupon) {
//        Notification notification = createNotification(receiver, type, coupon);
//
//        // 로그인 한 유저의 SseEmitter 모두 가져오기
//        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(receiver.getId());
//
//        sseEmitters.forEach(
//                (key, emitter) -> {
//                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
//                    emitterRepository.saveEventCache(key, notification);
//                    // 데이터 전송
//                    sendToClient(emitter, key, NotificationResponse.of(notification));
//                }
//        );
//    }
//
//    @Override
//    @Transactional
//    public void sendList(List<User> receiverList, NotificationType type, Reservation reservation) {
//        Map<String, SseEmitter> sseEmitters;
//
//        for (User receiver : receiverList) {
//
//            sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(receiver.getId());
//
//            Notification notification = createNotification(receiver, type, reservation);
//
//            sseEmitters.forEach(
//                    (key, emitter) -> {
//                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
//                        emitterRepository.saveEventCache(key, notification);
//                        // 데이터 전송
//                        sendToClient(emitter, key, NotificationResponse.of(notification));
//                    }
//            );
//        }
//    }

    @Override
    @Transactional
    public void sendList(List<User> receiverList, NotificationType type, Coupon coupon) {
        Map<String, SseEmitter> sseEmitters;

        for (User receiver : receiverList) {

            sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(receiver.getId());

            Notification notification = createNotification(receiver, type, coupon);

            sseEmitters.forEach(
                    (key, emitter) -> {
                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                        emitterRepository.saveEventCache(key, notification);
                        // 데이터 전송
                        sendToClient(emitter, key, NotificationResponse.of(notification));
                    }
            );
        }
    }

    @Override
    public Notification createNotification(User receiver, NotificationType type, Reservation reservation) {
        StringBuilder contentSb = new StringBuilder();
        String imageUrl = null;
        // YYYY년 MM월 DD일 HH시 MM분 예약이 고객 사정으로 인해 취소되었습니다.
        if (type.equals(NotificationType.CUSTOMER_RESERVATION_CANCEL)) {
            contentSb.append(reservation.getDatetime().getYear()).append("년 ")
                    .append(reservation.getDatetime().getMonthValue()).append("월 ")
                    .append(reservation.getDatetime().getDayOfMonth()).append("일 ")
                    .append(reservation.getDatetime().getHour()).append("시 ");
            if (reservation.getDatetime().getMinute() > 0)
                    contentSb.append(reservation.getDatetime().getMinute()).append("분 ");
            contentSb.append("예약이 고객 사정으로 인해 취소되었습니다.");
        } else if (type.equals(NotificationType.STORE_RESERVATION_CANCEL)) {
            // [XX] YYYY년 MM월 DD일 HH시 MM분 예약이 가게 사정으로 인해 취소되었습니다.
            contentSb.append("[").append(reservation.getStore().getStoreName()).append("] ");
            contentSb.append(reservation.getDatetime().getYear()).append("년 ")
                    .append(reservation.getDatetime().getMonthValue()).append("월 ")
                    .append(reservation.getDatetime().getDayOfMonth()).append("일 ")
                    .append(reservation.getDatetime().getHour()).append("시 ");
            if (reservation.getDatetime().getMinute() > 0)
                contentSb.append(reservation.getDatetime().getMinute()).append("분 ");

            contentSb.append("예약이 가게 사정으로 인해 취소되었습니다.");
            imageUrl = reservation.getStore().getProfileImage();
        } else if (type.equals(NotificationType.BEFORE_RESERVATION)) {
            // [XX] HH시 MM분 예약 2시간 전입니다.
            contentSb.append("[").append(reservation.getStore().getStoreName()).append("] ")
                    .append(reservation.getDatetime().getHour()).append("시 ");
            if (reservation.getDatetime().getMinute() > 0)
                contentSb.append(reservation.getDatetime().getMinute()).append("분 ");
            contentSb.append("예약 2시간 전입니다.");
            imageUrl = reservation.getStore().getProfileImage();
        } else if (type.equals(NotificationType.NEW_RESERVATION)) {
            // [XX] YYYY년 MM월 DD일 HH시 MM분에 새로운 예약이 추가되었습니다.
            contentSb.append(reservation.getDatetime().getYear()).append("년 ")
                    .append(reservation.getDatetime().getMonthValue()).append("월 ")
                    .append(reservation.getDatetime().getDayOfMonth()).append("일 ")
                    .append(reservation.getDatetime().getHour()).append("시 ")
                    .append(reservation.getDatetime().getMinute()).append("분 예약이 추가되었습니다.");
        }
        return notificationRepository.save(Notification.builder()
                .user(receiver)
                .type(type)
                .content(contentSb.toString())
                .relatedId(reservation.getId())
                .imageUrl(imageUrl)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    public Notification createNotification(User receiver, NotificationType type, Coupon coupon) {
        StringBuilder contentSb = new StringBuilder();
        contentSb.append("[").append(coupon.getStore().getStoreName()).append("] ")
                .append("지금부터 선착순 ").append(coupon.getQuantity()).append("분께 ")
                .append(coupon.getPayMoney()).append("원 이상 주문시 사용 가능한 ")
                .append(coupon.getDiscountMoney()).append("원 할인 쿠폰을 드립니다.");
        String imageUrl = coupon.getStore().getProfileImage();
        return notificationRepository.save(Notification.builder()
                .user(receiver)
                .type(type)
                .content(contentSb.toString())
                .relatedId(coupon.getId())
                .imageUrl(imageUrl)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

}
