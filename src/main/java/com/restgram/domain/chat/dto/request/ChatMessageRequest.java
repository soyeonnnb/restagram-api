package com.restgram.domain.chat.dto.request;

import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatMessageType;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(

        @NotNull(message = "수신자 ID는 필수영역입니다.")
        @Min(value = 0, message = "사용자 ID는 음수가 될 수 없습니다.")
        Long userId,

        @NotNull(message = "채팅방 ID는 필수영역입니다.")
        @Min(value = 0, message = "채팅방 ID는 음수가 될 수 없습니다.")
        Long roomId,

        @NotNull(message = "채팅방 타입은 필수영역입니다.")
        ChatMessageType type,

        @NotBlank(message = "채팅 메세지는 필수영역입니다.")
        @Size(min = 1, max = 1000, message = "채팅 메세지는 최소 1자 ~ 최대 1000자까지 입력 가능합니다")
        String message

) {

    public ChatMessage toEntity(User user, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .author(user)
                .chatRoom(chatRoom)
                .message(this.message)
                .type(this.type)
                .build();
    }
}
