package com.restgram.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ChatErrorCode implements ErrorCode{
    CHATROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "Chat room not found"),
    NOT_USERS_CHATROOM(HttpStatus.BAD_REQUEST, "User not participated this chat room")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
