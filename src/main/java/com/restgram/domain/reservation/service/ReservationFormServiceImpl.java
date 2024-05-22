package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormDateRequest;
import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(readOnly = true)
    public List<ReservationFormResponse> getReservationForm(Long storeId, Integer year, Integer month) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        LocalDate date = LocalDate.of(year, month, 1);
        List<ReservationForm> reservationFormList = reservationFormRepository.findAllByStoreAndDateBetweenAndStateEqualsAndRemainQuantityGreaterThan(store, date, date.plusMonths(1).minusDays(1), ReservationFormState.ACTIVE, 0);
        List<ReservationFormResponse> customerReservationFormResponseList = new ArrayList<>();
        for(ReservationForm form : reservationFormList) {
            customerReservationFormResponseList.add(ReservationFormResponse.of(form));
        }
        return customerReservationFormResponseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationFormResponse> getStoreReservationForm(Long storeId, Integer year, Integer month) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        LocalDate date = LocalDate.of(year, month, 1);
        List<ReservationForm> reservationFormList = reservationFormRepository.findAllByStoreAndDateBetween(store, date, date.plusMonths(1).minusDays(1));
        List<ReservationFormResponse> storeReservationFormResponseList = new ArrayList<>();
        for(ReservationForm form : reservationFormList) {
            storeReservationFormResponseList.add(ReservationFormResponse.of(form));
        }
        return storeReservationFormResponseList;
    }

    // 예약 폼 변경
    @Override
    @Transactional
    public void updateReservationState(Long storeId, UpdateReservationFormRequest request) {
        ReservationForm form = reservationFormRepository.findById(request.getId()).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        // 유저 일치 확인
        if (form.getStore().getId() != storeId) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        form.updateState(request.getState());
        reservationFormRepository.save(form);
    }

}