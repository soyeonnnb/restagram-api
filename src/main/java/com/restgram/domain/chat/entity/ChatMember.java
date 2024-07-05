package com.restgram.domain.chat.entity;

import com.restgram.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 채팅 유저

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방

    @ColumnDefault("0")
    @NotNull
    private Long unReadMessageCount;

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void addCount() {
        this.unReadMessageCount++;
    }

    public void resetUnReadMessageCount() {
        this.unReadMessageCount = 0L;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", chatRoom=" + chatRoom.getId() +
                ", unReadMessageCount=" + unReadMessageCount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        ChatMember that = (ChatMember) obj;
        return this.user.equals(that.user) && this.chatRoom.equals(that.chatRoom);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(user, chatRoom);
    }

}
