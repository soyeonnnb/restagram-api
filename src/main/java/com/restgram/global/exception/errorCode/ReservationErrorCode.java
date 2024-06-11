package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ReservationErrorCode implements ErrorCode {
    INVALID_RESERVATION_ID(HttpStatus.BAD_REQUEST, "RESERVATION-001", "유효하지 않은 예약 ID입니다."),
    INVALID_RESERVATION_FORM_ID(HttpStatus.BAD_REQUEST, "RESERVATION-002", "유효하지 않은 예약폼 ID입니다."),
    INVALID_RESERVATION_CANCEL_ID(HttpStatus.BAD_REQUEST, "RESERVATION-003", "유효하지 않은 예약 취소 ID입니다."),
    TABLE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "RESERVATION-004", "테이블 개수가 부족합니다."),
    RESERVATION_DISABLE(HttpStatus.BAD_REQUEST, "RESERVATION-005", "예약이 가능한 상태가 아닙니다"),
    RESERVATION_HEAD_COUNT_EXCEED_TABLE_NUM(HttpStatus.BAD_REQUEST, "RESERVATION-006", "인원 수가 초과되었습니다."),
    RESERVATION_IS_BEFORE_NOW(HttpStatus.BAD_REQUEST, "RESERVATION-007", "이전 예약폼은 수정 불가합니다."),
    RESERVATION_CANCEL_DISABLE(HttpStatus.BAD_REQUEST, "RESERVATION-008", "예약 취소가 불가능합니다.")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ReservationErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}