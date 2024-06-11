package com.restgram.global.exception.handler;

import com.restgram.global.exception.entity.ApiResponse;
import com.restgram.global.exception.entity.ErrorResponse;
import com.restgram.global.exception.entity.ErrorResponse.ValidationError;
import com.restgram.global.exception.entity.RestApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전역적으로 에러를 처리.
// Spring 내부에서 스프링 예외를 미리 처리해 둔 ResponseEntityExceptionHandler 존재.
// 이를 상속받아 사용.
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler { // extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<ApiResponse<?>> handleRestApiException(RestApiException e,
      HttpServletRequest request) {
    log.error("요청 실패 -> 요청 경로: {}, 로그메세지: {}", request.getRequestURL(), e.getLog());

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(ApiResponse.createError(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    List<ValidationError> errors = fieldErrors.stream()
        .map(ErrorResponse.ValidationError::of)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createError("ERROR-001", errors, e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument 에러 발생 Message: {}", e.getMessage());

    // 사용자에게 전달할 에러 메시지를 설정
    String errorMessage = "적절하지 않은 인자를 메소드에 넘겨주었습니다. 파라미터를 확인해주세요.";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createError("ERROR-002", errorMessage, e.getMessage()));
  }


  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(
      ConstraintViolationException e) {
    Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

    List<String> errors = constraintViolations.stream()
        .map(constraintViolation -> extractField(constraintViolation.getPropertyPath()) + ", "
            + constraintViolation.getMessage())
        .toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createError("ERROR-003", errors, e.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    log.error("요청 실패 => 요청 경로: {}, 오류메세지: {}", request.getRequestURL(), e.getMessage());

    // 사용자에게 전달할 에러 메시지를 설정
    String errorMessage = "잘못된 요청 본문 형식입니다. 입력 데이터를 확인해주세요.";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.createError("ERROR-004", errorMessage, e.getMessage()));
  }

  private String extractField(Path path) {
    String[] arrays = path.toString().split("[.]");
    int index = arrays.length - 1;
    return arrays[index];
  }
}
