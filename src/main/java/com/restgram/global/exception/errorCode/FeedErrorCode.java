package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum FeedErrorCode implements ErrorCode {
    INVALID_FEED_ID(HttpStatus.BAD_REQUEST, "FEED-001", "유효하지 않은 피드 ID입니다."),
    INVALID_FEED_IMAGE_ID(HttpStatus.BAD_REQUEST, "FEED-002", "유효하지 않은 피드 이미지 ID입니다."),
    INVALID_FEED_LIKE_ID(HttpStatus.BAD_REQUEST, "FEED-003", "유효하지 않은 피드 좋아요 ID입니다."),
    INVALID_FEED_CURSOR_ID(HttpStatus.BAD_REQUEST, "FEED-004", "유효하지 않은 피드 커서 ID입니다."),
    FEED_EMPTY(HttpStatus.BAD_REQUEST, "FEED-005", "데이터베이스에 피드가 존재하지 않습니다."),
    ALREADY_FEED_LIKE(HttpStatus.BAD_REQUEST, "FEED-006", "이미 사용자가 좋아요한 피드입니다.")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    FeedErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}