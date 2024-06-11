package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode{
    UPLOAD_EXCEPTION(HttpStatus.BAD_REQUEST, "S3-001", "S3 업로드 중 에러가 발생하였습니다.")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    S3ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
