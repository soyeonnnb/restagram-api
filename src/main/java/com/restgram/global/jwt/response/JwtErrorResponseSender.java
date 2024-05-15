package com.restgram.global.jwt.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restgram.global.exception.entity.ErrorResponse;
import com.restgram.global.exception.errorCode.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 오류 응답을 전송
@Component
public class JwtErrorResponseSender {
    public static void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        try {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code(errorCode.name())
                    .status(errorCode.getHttpStatus().value())
                    .message(errorCode.getMessage())
                    .build();

            // json 형식으로 내보냄
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(errorCode.getHttpStatus().value());

            // 에러 메세지 보내기
            new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}