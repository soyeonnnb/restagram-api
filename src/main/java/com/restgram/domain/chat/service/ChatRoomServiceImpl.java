package com.restgram.domain.chat.service;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;
import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMemberRepository;
import com.restgram.domain.chat.repository.ChatRoomRepository;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    // 채팅방 가져오기 (없으면 생성후 가져오기)
    @Override
    @Transactional
    public ChatRoomResponse getChatRoom(Long userId, Long receiverId) {
        if (userId == receiverId) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUsers(user, receiver);
        ChatRoom chatRoom;
        // 만약 이전에 생성되지 않은 채팅방이라면
        if (chatRoomOptional.isEmpty()) {
            // 멤버 생성
            ChatMember member1 = ChatMember.builder().user(user).build();
            ChatMember member2 = ChatMember.builder().user(receiver).build();

            // 채팅방 생성 후
            chatRoom = ChatRoom.builder()
                    .members(Arrays.asList(member1, member2))
                    .build();

            chatRoomRepository.save(chatRoom);

            // 멤버에 채팅방 넣기
            member1.setChatRoom(chatRoom);
            member2.setChatRoom(chatRoom);

            // 멤버 저장
            chatMemberRepository.save(member1);
            chatMemberRepository.save(member2);
        } else {
            chatRoom = chatRoomOptional.get();
        }
        ChatRoomResponse chatRoomResponse = ChatRoomResponse.of(chatRoom, receiver);
        return chatRoomResponse;
    }
}
