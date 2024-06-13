package com.restgram.domain.reservation.service.impl;

import com.restgram.domain.reservation.dto.request.ReservationFormDateRequest;
import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormStateRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.reservation.service.ReservationFormService;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ReservationErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationFormServiceImpl implements ReservationFormService {

    private final StoreRepository storeRepository;
    private final ReservationFormRepository reservationFormRepository;

    @Override
    @Transactional
    public void addReservationForm(Long userId, ReservationFormRequest request) {
        Store store = storeRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        // 하루씩 키워나가면서
        for (LocalDate date = request.startAt(); date.isBefore(request.finishAt().plusDays(1));
             date = date.plusDays(1)) {
            // 만약 제외 날짜에 포함되어 있다면 패스
            if (request.exceptDateList().contains(date)) {
                continue;
            }
            for (ReservationFormDateRequest datetable : request.weekListMap()
                    .getOrDefault(date.getDayOfWeek(), new ArrayList<>())) {
                // 만약 해당 날짜 + 시간에 이미 예약폼이 존재한다면 테이블 수 늘리기
                Optional<ReservationForm> optionalReservationForm = reservationFormRepository.findByStoreAndDateAndTime(
                        store, date, datetable.time());
                if (optionalReservationForm.isPresent()) {
                    optionalReservationForm.get().updateQuantity(datetable.table());
                    optionalReservationForm.get().updateRemainQuantity(datetable.table());
                } else {
                    ReservationForm form = ReservationForm.builder().store(store).date(date)
                            .time(datetable.time()).quantity(datetable.table())
                            .remainQuantity(datetable.table()).tablePerson(request.tablePerson())
                            .maxReservationPerson(request.maxReservationPerson())
                            .state(ReservationFormState.ACTIVE).build();
                    reservationFormRepository.save(form);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationFormResponse> getReservationForm(Long storeId, Integer year,
                                                            Integer month) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + storeId + "]"));
        // 해당 달
        LocalDate date = LocalDate.of(year, month, 1);

        List<ReservationForm> reservationFormList = reservationFormRepository.findAllByStoreAndDateBetweenAndStateEquals(
                store, date, date.plusMonths(1).minusDays(1), ReservationFormState.ACTIVE);

        return reservationFormList.stream().map(ReservationFormResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationFormResponse> getStoreReservationForm(Long storeId, Integer year,
                                                                 Integer month) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + storeId + "]"));
        // 해당 달
        LocalDate date = LocalDate.of(year, month, 1);

        List<ReservationForm> reservationFormList = reservationFormRepository.findAllByStoreAndDateBetween(
                store, date, date.plusMonths(1).minusDays(1));

        return reservationFormList.stream().map(ReservationFormResponse::of)
                .collect(Collectors.toList());
    }

    // 예약 폼 변경
    @Override
    @Transactional
    public void updateReservationState(Long storeId, UpdateReservationFormStateRequest request) {
        ReservationForm form = reservationFormRepository.findById(request.id())
                .orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_FORM_ID,
                        "예약폼ID가 유효하지 않습니다. [예약폼ID=" + request.id() + "]"));

        // 유저 일치 확인
        if (form.getStore().getId() != storeId) {
            throw new RestApiException(UserErrorCode.USER_MISMATCH,
                    "로그인 사용자와 예약폼 작성 가게 사용자ID가 일치하지 않습니다. [로그인 사용자ID=" + storeId + ", 예약폼 작성 가게 사용자ID="
                            + form.getStore().getId() + "]");
        }
        LocalDateTime formDateTime = LocalDateTime.of(form.getDate(), form.getTime());

        // 이미 시간이 지나간 예약폼은 수정 불가
        if (formDateTime.isBefore(LocalDateTime.now())) {
            throw new RestApiException(ReservationErrorCode.RESERVATION_IS_BEFORE_NOW,
                    "종료된 예약폼은 수정이 불가합니다. [예약폼ID=" + request.id() + ", 예약폼 날짜=" + form.getDate() + ", 시간="
                            + form.getTime() + "]");
        }

        form.updateState(request.state());
    }

}