package com.restgram.domain.chat.repository;

import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Optional<ChatMember> findByUserAndChatRoom(User user, ChatRoom chatRoom);
    
    Long getUnReadMessageCountByUserAndChatRoom(User user, ChatRoom chatRoom);
}
