package com.restgram.domain.chat.repository;

import com.restgram.domain.chat.controller.ChatRoomController;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr join cr.members cm1 join cr.members cm2 where cm1.user = :user and cm2.user = :receiver")
    @EntityGraph(attributePaths = {"lastMessage"})
    Optional<ChatRoom> findByUsers(User user, User receiver);


}
