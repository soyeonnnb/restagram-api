package com.restgram.domain.chat.service;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;

public interface ChatRoomService {
    ChatRoomResponse getChatRoom(Long userId, Long receiverId);
}
