package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum FollowErrorCode implements ErrorCode{
    INVALID_FOLLOW_ID(HttpStatus.BAD_REQUEST, "FOLLOW-001", "팔로우 ID가 유효하지 않습니다."),
    FOLLOW_FOLLOWING_IS_THE_SAME(HttpStatus.BAD_REQUEST, "FOLLOW-002", "팔로워 사용자와 팔로잉 사용자가 같습니다."),
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "FOLLOW-003", "이미 팔로우한 사용자입니다")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    FollowErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
