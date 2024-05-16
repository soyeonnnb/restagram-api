package com.restgram.global.exception.entity;

import com.restgram.global.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
