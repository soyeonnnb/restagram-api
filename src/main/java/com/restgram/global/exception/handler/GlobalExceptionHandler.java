package com.restgram.global.exception.handler;

import com.restgram.global.exception.entity.ErrorResponse;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// 전역적으로 에러를 처리.
// Spring 내부에서 스프링 예외를 미리 처리해 둔 ResponseEntityExceptionHandler 존재.
// 이를 상속받아 사용.
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class) // 어떤 에러를 처리할건지
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        log.warn("handleException Message: {}", e.getErrorCode().getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class) // @Valid에서 잘못된 파라미터 입력시
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument Message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .status(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .status(errorCode.getHttpStatus().value())
                .message(message)
                .build();
    }
}
