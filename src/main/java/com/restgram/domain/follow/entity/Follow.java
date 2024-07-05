package com.restgram.domain.follow.entity;

import com.restgram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "follower_id", nullable = false)
    @ManyToOne
    private User follower; // 팔로우 한 사람

    @JoinColumn(name = "following_id", nullable = false)
    @ManyToOne
    private User following; // 팔로우 당한 사람

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        Follow that = (Follow) obj;

        return this.follower.equals(that.follower) &&
                this.following.equals(that.following)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.follower, this.following);
    }
}
