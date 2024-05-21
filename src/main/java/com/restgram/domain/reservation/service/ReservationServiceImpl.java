package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.entity.*;
import com.restgram.domain.reservation.repository.ReservationCancelRepository;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.ReservationErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final CustomerRepository customerRepository;
    private final ReservationFormRepository reservationFormRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCancelRepository reservationCancelRepository;

    @Override
    @Transactional
    public void addReservation(Long userId, AddReservationRequest request) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ReservationForm form = reservationFormRepository.findById(request.getReservationFormId()).orElseThrow(() -> new RestApiException(ReservationErrorCode.RESERVATION_FORM_NOT_FOUND));
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
        reservationFormRepository.save(form);
    }

    @Override
    @Transactional
    public void cancelReservation(Long userId, DeleteReservationRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId()).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        // 만약 해당 유저와 관계 없는 예약이면 삭제 취소 불가
        if ((request.getState().equals(ReservationCancelState.CUSTOMER) && reservation.getCustomer().getId() != userId)
            || (request.getState().equals(ReservationCancelState.STORE) && reservation.getStore().getId() != userId))
            throw new RestApiException(UserErrorCode.USER_MISMATCH);

        // 이미 취소처리되었거나 기간이 지났으면 예약이면 취소 불가
        if (!reservation.getState().equals(ReservationState.ACTIVE) || reservation.getDatetime().isBefore(LocalDateTime.now())) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);

        ReservationCancel reservationCancel = request.of(reservation);

        // 삭제 처리
        if (request.getState().equals(ReservationCancelState.CUSTOMER)) reservation.updateState(ReservationState.USER_CANCELED);
        else if (request.getState().equals(ReservationCancelState.STORE)) reservation.updateState(ReservationState.STORE_CANCELED);

        // 테이블 수 업데이트
        reservation.getReservationForm().updateRemainQuantity(getTableNum(reservation.getHeadCount(), reservation.getReservationForm().getTablePerson()));
        
        reservationCancelRepository.save(reservationCancel);
        reservationRepository.save(reservation);
    }

    private int getTableNum(Integer headcount, Integer tablePerson) {
        Integer table = headcount / tablePerson;
        if (headcount%tablePerson != 0) table++;
        return table;
    }
}
