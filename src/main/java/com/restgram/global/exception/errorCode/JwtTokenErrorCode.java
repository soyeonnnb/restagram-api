package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum JwtTokenErrorCode implements ErrorCode {
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token is expired"), // 만료된 토큰
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token is expired"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid token signature"),// 잘못된 토큰 서명
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported token format"), // 토큰 포맷이 잘못됨
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token"), // 잘못된 토큰
    DOES_NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "Does not exist token") // 잘못된 토큰
    ;

    private final HttpStatus httpStatus;
    private final String message;

}