package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found"),
    USER_DUPLICATED(HttpStatus.BAD_REQUEST, "User already exists"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password mismatched"),
    INVALID_USER_CODE(HttpStatus.BAD_REQUEST, "User code is invalid"),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "User Email is duplicated"),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "Username is duplicated")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
