package com.restgram.domain.reservation.service.impl;

import com.restgram.domain.calendar.repository.CalendarEventRepository;
import com.restgram.domain.calendar.service.CalendarEventService;
import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.CustomerReservationResponse;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;
import com.restgram.domain.reservation.entity.*;
import com.restgram.domain.reservation.repository.ReservationCancelRepository;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ReservationErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ReservationFormRepository reservationFormRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCancelRepository reservationCancelRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final CalendarEventService calendarEventService;
    private final CalendarEventRepository calendarEventRepository;

    @Override
    @Transactional
    public void addReservation(Long userId, AddReservationRequest request) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        ReservationForm form = reservationFormRepository.findById(request.getReservationFormId()).orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_FORM_ID));
        // 예약 일자 확인
        LocalDateTime formDate = LocalDateTime.of(form.getDate(), form.getTime());
        if (formDate.isBefore(LocalDateTime.now())) throw new RestApiException(ReservationErrorCode.RESERVATION_IS_BEFORE_NOW);
        // 테이블이 가능한지 확인
        if (form.getState().toString().equals(ReservationFormState.DISABLE)) throw new RestApiException(ReservationErrorCode.RESERVATION_DISABLE);
        // 테이블 수 확인
        int tableNum = getTableNum(request.getHeadCount(), form.getTablePerson());
        if (form.getRemainQuantity() < tableNum) {
            throw new RestApiException(ReservationErrorCode.TABLE_UNAVAILABLE);
        }
        // 예약 가능 수 확인
        if (form.getMaxReservationPerson() < request.getHeadCount()) throw new RestApiException(ReservationErrorCode.RESERVATION_HEAD_COUNT_EXCEED_TABLE_NUM);

        // 예약 생성
        Reservation reservation = request.of(form, form.getStore(), customer);
        reservationRepository.save(reservation);
        form.updateRemainQuantity(tableNum * -1);

        // 예약 생성 알림 보내기
        notificationService.send(reservation.getStore(), NotificationType.NEW_RESERVATION, reservation);

        // 만약 캘린더 동의가 되어 있다면 일정에 추가하기
        if (customer.isCalendarAgree()) {
            calendarEventService.createCalendarEvent(reservation);
        }

    }

    @Override
    @Transactional
    public void cancelReservation(Long userId, DeleteReservationRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        Reservation reservation = reservationRepository.findById(request.getReservationId()).orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_ID));
        // 만약 해당 유저와 관계 없는 예약이면 삭제 취소 불가
        if ((request.getState().equals(ReservationCancelState.CUSTOMER) && reservation.getCustomer().getId() != userId)
            || (request.getState().equals(ReservationCancelState.STORE) && reservation.getStore().getId() != userId))
            throw new RestApiException(UserErrorCode.USER_MISMATCH);

        // 이미 취소처리되었거나 기간이 지났으면 예약이면 취소 불가
        if (!reservation.getState().equals(ReservationState.ACTIVE) || reservation.getDatetime().isBefore(LocalDateTime.now())) throw new RestApiException(ReservationErrorCode.RESERVATION_CANCEL_DISABLE);

        ReservationCancel reservationCancel = request.of(reservation);

        // 삭제 처리
        if (request.getState().equals(ReservationCancelState.CUSTOMER)) reservation.updateState(ReservationState.USER_CANCELED);
        else if (request.getState().equals(ReservationCancelState.STORE)) reservation.updateState(ReservationState.STORE_CANCELED);

        // 테이블 수 업데이트
        reservation.getReservationForm().updateRemainQuantity(getTableNum(reservation.getHeadCount(), reservation.getReservationForm().getTablePerson()));

        // 엔티티 수정& 저장
        reservationCancelRepository.save(reservationCancel);

        // 알림 보내기
        if (request.getState().equals(ReservationCancelState.CUSTOMER)) {
            notificationService.send(reservation.getStore(), NotificationType.CUSTOMER_RESERVATION_CANCEL, reservation);
        } else {
            notificationService.send(reservation.getCustomer(), NotificationType.STORE_RESERVATION_CANCEL, reservation);
        }

        // 카카오 일정 삭제
        if (calendarEventRepository.existsByReservation(reservation)) {
            calendarEventService.deleteCalendarEvent(reservation);
        }
    }

    // 구매자 예약 리스트
    @Override
    public List<CustomerReservationResponse> getCustomerReservations(Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        List<Reservation> reservationList = reservationRepository.findAllByCustomerOrderByDatetimeDesc(customer);
        List<CustomerReservationResponse> customerReservationResponseList = new ArrayList<>();
        for(Reservation reservation : reservationList) {
            if (reservation.getState().equals(ReservationState.ACTIVE)) {
                customerReservationResponseList.add(CustomerReservationResponse.of(reservation));
            } else {
                ReservationCancel reservationCancel = reservationCancelRepository.findByReservation(reservation).orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_CANCEL_ID));
                customerReservationResponseList.add(CustomerReservationResponse.of(reservation, reservationCancel.getMemo()));
            }
        }
        return customerReservationResponseList;
    }

    @Override
    public List<StoreReservationResponse> getStoreReservations(Long userId, Integer year, Integer month) {
        Store store = storeRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        LocalDateTime startAt = LocalDateTime.of(year, month, 1, 0, 0);
        // 해당 가게의, 연도/월 검색
        List<Reservation> reservationList = reservationRepository.findAllByStoreAndDatetimeGreaterThanEqualAndDatetimeLessThanOrderByDatetime(store, startAt, startAt.plusMonths(1));
        List<StoreReservationResponse> storeReservationResponseList = new ArrayList<>();
        for(Reservation reservation : reservationList) {
            if (reservation.getState().equals(ReservationState.ACTIVE)) {
                storeReservationResponseList.add(StoreReservationResponse.of(reservation));
            } else {
                ReservationCancel reservationCancel = reservationCancelRepository.findByReservation(reservation).orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_CANCEL_ID));
                storeReservationResponseList.add(StoreReservationResponse.of(reservation, reservationCancel.getMemo()));
            }
        }
        return storeReservationResponseList;
    }

    private int getTableNum(Integer headcount, Integer tablePerson) {
        Integer table = headcount / tablePerson;
        if (headcount%tablePerson != 0) table++;
        return table;
    }
}
