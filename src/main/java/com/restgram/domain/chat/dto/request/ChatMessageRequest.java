package com.restgram.domain.chat.dto.request;

import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatMessageType;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private Long userId;
    private Long roomId;
    private ChatMessageType type;
    private String message;

    public ChatMessage of(User user, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .author(user)
                .chatRoom(chatRoom)
                .message(this.message)
                .type(this.type)
                .build();
    }
}
