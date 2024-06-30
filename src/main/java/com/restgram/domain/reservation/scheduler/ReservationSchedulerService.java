package com.restgram.domain.reservation.scheduler;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationSchedulerService {

    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    // 2시간 전 예약 알림
//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul") // 매 분 확인해야함
    @Transactional
    public void reservationBefore2HoursSchedule() {
        try {
            // 현재 날짜를 가져옵니다.
            LocalDate dateNow = LocalDate.now();

            // 현재 시각의 시와 분을 가져오고, 초와 나노초를 0으로 설정합니다.
            LocalTime timeNow = LocalTime.of(LocalTime.now().plusHours(2).getHour(), LocalTime.now().getMinute());

            // 현재 날짜와 시각을 결합하여 LocalDateTime 객체를 만듭니다.
            LocalDateTime dateTimeNow = LocalDateTime.of(dateNow, timeNow);

            List<Reservation> reservationList = reservationRepository.findAllByDatetimeAndState(dateTimeNow, ReservationState.ACTIVE);
            log.info("{} 예약 알림 보내기 시작", dateTimeNow);
            for (Reservation reservation : reservationList) {
                notificationService.send(reservation.getCustomer(), NotificationType.BEFORE_RESERVATION, reservation);
            }
            log.info("{} 예약 알림 보내기 성공", dateTimeNow);
        } catch (Exception e) {
            log.error("예약 2시간 전 알람 스케줄 중 예외 발생", e);
        }
    }

}
