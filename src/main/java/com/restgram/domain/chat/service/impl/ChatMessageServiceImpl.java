package com.restgram.domain.chat.service.impl;

import com.restgram.domain.chat.dto.response.ChatMessageResponse;
import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMemberRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;


    @Override
    @Transactional
    public List<ChatMessageResponse> getChatList(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RestApiException(ChatErrorCode.INVALID_CHATROOM_ID, "채팅방ID가 유효하지 않습니다. [채팅방ID=" + roomId + "]"));
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoom(chatRoom);

        // 리스트 변환
        List<ChatMessageResponse> chatMessageResponseList = chatMessageList.stream().map(ChatMessageResponse::of).collect(Collectors.toList());
        ChatMember chatMember = chatMemberRepository.findByUserAndChatRoom(user, chatRoom).orElseThrow(() -> new RestApiException(ChatErrorCode.INVALID_CHAT_MEMBER));

        // 채팅 리스트를 가져옴 => 모든 채팅을 읽음
        chatMember.resetUnReadMessageCount();
        return chatMessageResponseList;
    }

}
