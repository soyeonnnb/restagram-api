package com.restgram.domain.chat.service.impl;

import com.restgram.domain.chat.dto.response.ChatMessageResponse;
import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMessageRepository;
import com.restgram.domain.chat.repository.ChatRoomRepository;
import com.restgram.domain.chat.service.ChatMessageService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Override
    public List<ChatMessageResponse> getChatList(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RestApiException(ChatErrorCode.INVALID_CHATROOM_ID, "채팅방ID가 유효하지 않습니다. [채팅방ID=" + roomId + "]"));
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoom(chatRoom);

        // 리스트 변환
        List<ChatMessageResponse> chatMessageResponseList = chatMessageList.stream().map(ChatMessageResponse::of).collect(Collectors.toList());

        return chatMessageResponseList;
    }

}
