package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode{
    UPLOAD_EXCEPTION(HttpStatus.BAD_REQUEST, "Exception occurred while uploading")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
