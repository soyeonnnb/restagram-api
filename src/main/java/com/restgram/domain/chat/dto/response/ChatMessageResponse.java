package com.restgram.domain.chat.dto.response;

import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatMessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long id;
    private ChatMessageType type; // IMAGE, TALK
    private String message;
    private Long authorId;
    private LocalDateTime time;

    public static ChatMessageResponse of(ChatMessage chatMessage) {
        if (chatMessage == null) return null;
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .authorId(chatMessage.getAuthor().getId())
                .time(chatMessage.getTime())
                .build();
    }
}
