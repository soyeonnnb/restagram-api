package com.restgram.domain.chat.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatSendResponse(

//        List<Long> userIds,
        ChatMessageResponse message,
//        ChatRoomResponse chatRoomResponse,
        List<ChatMemberResponse> chatMemberResponseList

) {

}
