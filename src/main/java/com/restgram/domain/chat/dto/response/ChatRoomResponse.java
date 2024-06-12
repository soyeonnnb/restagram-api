package com.restgram.domain.chat.dto.response;

import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomResponse(

    Long id,
    ChatMessageResponse lastMessage,
    UserInfoResponse receiver,
    LocalDateTime createdAt
    
) {

  public static ChatRoomResponse of(ChatRoom chatRoom, User receiver) {
    return ChatRoomResponse.builder()
        .id(chatRoom.getId())
        .lastMessage(ChatMessageResponse.of(chatRoom.getLastMessage()))
        .receiver(UserInfoResponse.of(receiver))
        .createdAt(chatRoom.getCreatedAt())
        .build();
  }

}