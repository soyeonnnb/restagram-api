package com.restgram.domain.chat.service;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponse getChatRoom(Long userId, Long receiverId);
    List<ChatRoomResponse> getChatRoomList(Long userId);
}
