package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AddressErrorCode implements ErrorCode{
    INVALID_BCODE(HttpStatus.BAD_REQUEST, "Invalid bcode")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}

