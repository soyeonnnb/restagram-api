package com.restgram.domain.chat.service;

import com.restgram.domain.chat.dto.request.ChatMessageRequest;
import com.restgram.domain.chat.dto.response.ChatSendResponse;

public interface ChatService {
    ChatSendResponse sendChat(ChatMessageRequest request);
}
