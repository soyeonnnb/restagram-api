package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ChatErrorCode implements ErrorCode{
    INVALID_CHATROOM_ID(HttpStatus.BAD_REQUEST, "CHAT-001", "채팅방 ID가 유효하지 않습니다."),
    NOT_USER_CHATROOM(HttpStatus.BAD_REQUEST, "CHAT-002", "로그인 유저가 참여한 채팅방이 아닙니다"),
    SENDER_RECEIVER_IS_THE_SAME(HttpStatus.BAD_REQUEST, "CHAT-003", "채팅 수신자와 발신자가 동일합니다.")
    ;
    
    private HttpStatus httpStatus;
    private String code;
    private String message;

    ChatErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
