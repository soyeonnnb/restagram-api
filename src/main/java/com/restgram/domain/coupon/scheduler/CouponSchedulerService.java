package com.restgram.domain.coupon.scheduler;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.User;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponSchedulerService {

    private final CouponRepository couponRepository;
    private final NotificationService notificationService;
    private final FollowRepository followRepository;

    // 쿠폰 발생 시작 시 알림
//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul") // 매 분 확인해야함
    @Transactional
    public void reservationBefore2HoursSchedule() {
        try {
            // 현재 날짜를 가져옵니다.
            LocalDate dateNow = LocalDate.now();

            // 현재 시각의 시와 분을 가져오고, 초와 나노초를 0으로 설정합니다.
            LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());

            // 현재 날짜와 시각을 결합하여 LocalDateTime 객체를 만듭니다.
            LocalDateTime dateTimeNow = LocalDateTime.of(dateNow, timeNow);

            List<Coupon> couponList = couponRepository.findAllByStartAtAndDisable(dateTimeNow, false);

            log.info("{} 쿠폰 알림 보내기 시작", dateTimeNow);
            for(Coupon coupon : couponList) {
                List<User> userList = followRepository.findFollowersByFollowing(coupon.getStore());
                notificationService.sendList(userList, NotificationType.START_COUPON, coupon);
            }
            log.info("{} 쿠폰 알림 보내기 성공", dateTimeNow);
        } catch (Exception e) {
            log.error("쿠폰 알림 스케줄 중 예외 발생", e);
        }
    }

}
