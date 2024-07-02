package com.restgram.domain.chat.dto.response;

import com.restgram.domain.chat.entity.ChatMember;
import lombok.Builder;


@Builder
public record ChatMemberResponse(
        Long id,
        ChatRoomResponse chatRoomResponse
) {

    public static ChatMemberResponse of(ChatMember me, ChatMember chatMember) {
        return ChatMemberResponse.builder()
                .id(me.getUser().getId())
                .chatRoomResponse(ChatRoomResponse.of(chatMember, me.getUnReadMessageCount()))
                .build();
    }
}
