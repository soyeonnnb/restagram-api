package com.restgram.domain.chat.repository;

import com.restgram.domain.chat.controller.ChatRoomController;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr join cr.members cm1 join cr.members cm2 where cm1.user = :user and cm2.user = :receiver")
    @EntityGraph(attributePaths = {"lastMessage"})
    Optional<ChatRoom> findByUsers(User user, User receiver);

    @Query("select cr from ChatRoom cr join cr.members cm1 where cm1.user = :user and cr.lastMessage != null")
    @EntityGraph(attributePaths = {"lastMessage"})
    List<ChatRoom> findAllByUser(User user);

    @EntityGraph(attributePaths = {"lastMessage", "members"})
    Optional<ChatRoom> findById(Long id);
}
