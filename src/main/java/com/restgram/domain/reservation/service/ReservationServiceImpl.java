package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ReservationErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final CustomerRepository customerRepository;
    private final ReservationFormRepository reservationFormRepository;
    private final ReservationRepository reservationRepository;

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
        form.updateRemainQuantity(tableNum);
        reservationFormRepository.save(form);
    }

    private int getTableNum(Integer headcount, Integer tablePerson) {
        Integer table = headcount / tablePerson;
        if (headcount%tablePerson != 0) table++;
        return table;
    }
}
