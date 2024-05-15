package com.restgram.global.exception.errorCode;

import org.springframework.http.HttpStatus;

// ErrorCode의 추상 메서드
public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
