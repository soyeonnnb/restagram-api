package com.restgram.domain.chat.dto.response;

import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomResponse(

        Long id,
        ChatMessageResponse lastMessage,
        UserInfoResponse receiver,
        LocalDateTime createdAt,
        Long unReadMessageCount

) {

    public static ChatRoomResponse of(ChatRoom chatRoom, User receiver, Long unReadMessageCount) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .lastMessage(ChatMessageResponse.of(chatRoom.getLastMessage()))
                .receiver(UserInfoResponse.of(receiver))
                .createdAt(chatRoom.getCreatedAt())
                .unReadMessageCount(unReadMessageCount)
                .build();
    }

    public static ChatRoomResponse of(ChatMember chatMember) {
        return ChatRoomResponse.builder()
                .id(chatMember.getChatRoom().getId())
                .lastMessage(ChatMessageResponse.of(chatMember.getChatRoom().getLastMessage()))
                .receiver(UserInfoResponse.of(chatMember.getUser()))
                .createdAt(chatMember.getChatRoom().getCreatedAt())
                .unReadMessageCount(chatMember.getUnReadMessageCount())
                .build();
    }


    public static ChatRoomResponse of(ChatMember chatMember, Long unReadMessageCount) {
        return ChatRoomResponse.builder()
                .id(chatMember.getChatRoom().getId())
                .lastMessage(ChatMessageResponse.of(chatMember.getChatRoom().getLastMessage()))
                .receiver(UserInfoResponse.of(chatMember.getUser()))
                .createdAt(chatMember.getChatRoom().getCreatedAt())
                .unReadMessageCount(unReadMessageCount)
                .build();
    }
}