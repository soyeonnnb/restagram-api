package com.restgram.domain.chat.service;

import com.restgram.domain.chat.dto.response.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageResponse> getChatList(Long userId, Long roomId);
}
