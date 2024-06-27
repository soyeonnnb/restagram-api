package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum CouponErrorCode implements ErrorCode {
    INVALID_COUPON_ID(HttpStatus.BAD_REQUEST, "COUPON-001", "쿠폰ID가 유효하지 않습니다."),
    INVALID_COUPON_ISSUE_ID(HttpStatus.BAD_REQUEST, "COUPON-002", "발급 쿠폰ID가 유효하지 않습니다."),
    REMAIN_QUANTITY_ZERO(HttpStatus.BAD_REQUEST, "COUPON-003", "쿠폰 잔여 수량이 0입니다."),
    FINISH_COUPON_ISSUE(HttpStatus.BAD_REQUEST, "COUPON-004", "발급이 종료된 쿠폰입니다."),
    NOT_ISSUE_PERIOD(HttpStatus.BAD_REQUEST, "COUPON-005", "발급 기간이 아닌 쿠폰입니다."),
    ALREADY_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "COUPON-006", "이미 발급된 쿠폰입니다."),
    INVALID_COUPON_START_AT(HttpStatus.BAD_REQUEST, "COUPON-007", "쿠폰 발급일자가 유효하지 않습니다."),
    ALREADY_USED_COUPON(HttpStatus.BAD_REQUEST, "COUPON-008", "이미 사용된 쿠폰입니다."),
    EXPIRED_COUPON(HttpStatus.BAD_REQUEST, "COUPON-009", "이미 만료된 쿠폰입니다."),
    INVALID_COUPON_USER(HttpStatus.BAD_REQUEST, "COUPON-010", "로그인 유저와 쿠폰 소유자가 일치하지 않습니다."),
    
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    CouponErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
