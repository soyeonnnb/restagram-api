package com.restgram.domain.chat.service.impl;

import com.restgram.domain.chat.dto.request.ChatMessageRequest;
import com.restgram.domain.chat.dto.response.ChatMessageResponse;
import com.restgram.domain.chat.dto.response.ChatSendResponse;
import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMessageRepository;
import com.restgram.domain.chat.repository.ChatRoomRepository;
import com.restgram.domain.chat.service.ChatMessageService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ChatErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatSendResponse sendChat(ChatMessageRequest request) {
        User sender = userRepository.findById(request.getUserId()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId()).orElseThrow(() -> new RestApiException(ChatErrorCode.CHATROOM_NOT_FOUND));

        // 해당 유저가 참가한 채팅방이 아니면
        if (!chatRoom.getMembers().contains(sender)) throw new RestApiException(ChatErrorCode.NOT_USERS_CHATROOM);

        // 메세지 저장
        ChatMessage message = request.of(sender, chatRoom);
        chatMessageRepository.save(message);

        // 메세지 설정
        chatRoom.updateLastMessage(message);

        // 유저 id만 가져와서 저장
        List<Long> ids = new ArrayList<>();
        for(ChatMember user : chatRoom.getMembers()) ids.add(user.getUser().getId()); 
        ChatSendResponse response = ChatSendResponse.builder()
                .message(ChatMessageResponse.of(message))
                .userIds(ids)
                .build();

        return response;
    }
}
