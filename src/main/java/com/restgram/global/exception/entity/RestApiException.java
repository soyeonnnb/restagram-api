package com.restgram.global.exception.entity;

import com.restgram.global.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private final ErrorCode errorCode;
    private String log;

    public RestApiException(ErrorCode errorCode, String log) {
        this.errorCode = errorCode;
        this.log = log;
    }
}
