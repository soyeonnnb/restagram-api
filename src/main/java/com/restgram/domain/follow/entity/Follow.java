package com.restgram.domain.follow.entity;

import com.restgram.domain.user.entity.User;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "follower_id", nullable = false)
    @ManyToOne
    private User follower; // 팔로우 한 사람

    @JoinColumn(name = "following_id", nullable = false)
    @ManyToOne
    private User following; // 팔로우 당한 사람

}
