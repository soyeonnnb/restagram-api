package com.restgram.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatSendResponse {
    private List<Long> userIds;
    private ChatMessageResponse message;
}
