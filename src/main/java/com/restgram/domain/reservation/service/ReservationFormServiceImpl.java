package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormDateRequest;
import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReservationFormServiceImpl implements ReservationFormService{

    private final StoreRepository storeRepository;
    private final ReservationFormRepository reservationFormRepository;

    @Override
    @Transactional
    public void addReservationForm(Long userId, ReservationFormRequest request) {
        Store store = storeRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 하루씩 키워나가면서
        for(LocalDate date = request.getStartAt(); date.isBefore(request.getFinishAt().plusDays(1)); date = date.plusDays(1)) {
            // 만약 제외 날짜에 포함되어 있다면 패스
            if (request.getExceptDateList().contains(date)) continue;
            // 이미 등록된 날짜라면 패스
            if (reservationFormRepository.existsByStoreAndDate(store, date)) continue;

            for(ReservationFormDateRequest datetable : request.getWeekListMap().getOrDefault(date.getDayOfWeek(), new ArrayList<>())) {
                ReservationForm form = ReservationForm.builder()
                        .store(store)
                        .date(date)
                        .time(datetable.getTime())
                        .quantity(datetable.getTable())
                        .remainQuantity(datetable.getTable())
                        .tablePerson(request.getTablePerson())
                        .maxReservationPerson(request.getMaxReservationPerson())
                        .state(ReservationFormState.ACTIVE)
                        .build();
                reservationFormRepository.save(form);
            }
        }
    }
}