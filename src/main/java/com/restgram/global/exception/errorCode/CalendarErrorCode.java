package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CalendarErrorCode implements ErrorCode{
    INVALID_CALENDAR_ID(HttpStatus.BAD_REQUEST, "CALENDAR-001", "카카오 캘린더ID가 유효하지 않습니다."),
    INVALID_CALENDAR_EVENT_ID(HttpStatus.BAD_REQUEST, "CALENDAR-002", "카카오 일정 이벤트ID가 유효하지 않습니다."),
    CALENDER_NOT_AUTHORIZATION(HttpStatus.BAD_REQUEST, "CALENDAR-003", "카카오 내에서 캘린더 동의항목에 동의하지 않았습니다."),
    ;
    
    private HttpStatus httpStatus;
    private String code;
    private String message;

    CalendarErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
