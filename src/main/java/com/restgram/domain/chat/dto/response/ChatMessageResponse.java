package com.restgram.domain.chat.dto.response;

import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatMessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageResponse(

    Long id,
    ChatMessageType type, // IMAGE, TALK
    String message,
    Long authorId,
    LocalDateTime time
    
) {

  public static ChatMessageResponse of(ChatMessage chatMessage) {
    if (chatMessage == null) {
      return null;
    }
    return ChatMessageResponse.builder()
        .id(chatMessage.getId())
        .type(chatMessage.getType())
        .message(chatMessage.getMessage())
        .authorId(chatMessage.getAuthor().getId())
        .time(chatMessage.getTime())
        .build();
  }
}
