package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AddressErrorCode implements ErrorCode{
    INVALID_SIDO_ID(HttpStatus.BAD_REQUEST, "ADDRESS-001", "시도 주소ID가 유효하지 않습니다"),
    INVALID_SIGG_ID(HttpStatus.BAD_REQUEST, "ADDRESS-002", "시군구 주소ID가 유효하지 않습니다"),
    INVALID_EMD_ID(HttpStatus.BAD_REQUEST, "ADDRESS-003", "읍면동 주소ID가 유효하지 않습니다"),
    INVALID_BCODE(HttpStatus.BAD_REQUEST, "ADDRESS-004", "주소 BCODE가 유효하지 않습니다")
    ;
    
    private HttpStatus httpStatus;
    private String code;
    private String message;

    AddressErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}

