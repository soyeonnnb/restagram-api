package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CalendarErrorCode implements ErrorCode{
    CALENDAR_NOT_FOUND(HttpStatus.BAD_REQUEST, "카카오 캘린더를 찾을 수 없습니다."),
    CALENDER_NOT_AUTHORIZATION(HttpStatus.BAD_REQUEST, "카카오 내에서 캘린더 동의항목에 동의하지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
