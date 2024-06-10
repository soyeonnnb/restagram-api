package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;
import com.restgram.domain.reservation.service.ReservationFormService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation/form")
@Slf4j
public class ReservationFormController {

    private final ReservationFormService reservationFormService;

    // 가게 예약 등록
    @PostMapping
    public CommonResponse addReservation(Authentication authentication, @RequestBody ReservationFormRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationFormService.addReservationForm(userId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("가계 예약 생성 성공")
                .build();
    }

    // 구매자 -> 가게 예약 폼 가져오기
    @GetMapping("/{storeId}")
    public CommonResponse getReservationForm(@PathVariable @NonNull Long storeId, @RequestParam @NonNull Integer year, @RequestParam @NonNull Integer month) {
        List<ReservationFormResponse> response = reservationFormService.getReservationForm(storeId, year, month);
        return CommonResponse.builder()
                .data(response)
                .success(true)
                .code(HttpStatus.OK.value())
                .message("구매자 -> 가게 예약 폼 가져오기 완료")
                .build();
    }

    // 가게 -> 자기가게
    @GetMapping
    public CommonResponse getStoreReservationForm(Authentication authentication, @RequestParam @NonNull Integer year, @RequestParam @NonNull Integer month) {
        Long storeId = Long.parseLong(authentication.getName());
        List<ReservationFormResponse> response = reservationFormService.getStoreReservationForm(storeId, year, month);
        return CommonResponse.builder()
                .data(response)
                .success(true)
                .code(HttpStatus.OK.value())
                .message("본인 가게 예약 폼 가져오기 성공")
                .build();
    }

    // 예약 상태 변경
    @PatchMapping
    public CommonResponse getUserReservationFormState(Authentication authentication, @RequestBody UpdateReservationFormRequest request) {
        Long storeId = Long.parseLong(authentication.getName());
        reservationFormService.updateReservationState(storeId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("가게 예약 폼 상태 변경 완료")
                .build();
    }
}
