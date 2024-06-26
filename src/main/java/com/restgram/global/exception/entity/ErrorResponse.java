package com.restgram.global.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@AllArgsConstructor
@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Integer status;

    
    // @Valid 어노테이션 사용하면서 에러 발생 시,
    // 어느 필드에서 에러가 발생했는지 응답을 위함
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

    }

}
