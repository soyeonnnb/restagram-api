package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_FORM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ReservationForm is not found"),
    TABLE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "테이블 개수가 부족합니다."),
    RESERVATION_DISABLE(HttpStatus.BAD_REQUEST, "예약이 가능한 상태가 아닙니다"),
    RESERVATION_HEAD_COUNT_EXCEED_TABLE_NUM(HttpStatus.BAD_REQUEST, "인원 수가 초과되었습니다."),
    RESERVATION_IS_BEFORE_NOW(HttpStatus.BAD_REQUEST, "이전 예약폼은 수정 불가합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}