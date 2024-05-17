package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum CouponErrorCode implements ErrorCode {
    REMAIN_QUANTITY_ZERO(HttpStatus.BAD_REQUEST, "Remain quantity is zero"), // 잔여 수량 0
    FINISH_COUPON_ISSUE(HttpStatus.BAD_REQUEST, "Finish coupon issue"),// 발급 종료
    NOT_ISSUE_PERIOD(HttpStatus.BAD_REQUEST, "Not within the issuance period"), // 발급 기간이 아님
    ALREADY_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "Already issued coupon") // 이미 발급
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
