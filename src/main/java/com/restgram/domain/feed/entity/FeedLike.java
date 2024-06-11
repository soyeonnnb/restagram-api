package com.restgram.domain.feed.entity;

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
public class FeedLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "feed_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed; // 피드

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 피드 유저
}
