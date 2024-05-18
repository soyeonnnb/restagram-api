package com.restgram.domain.feed.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_post_feed", columnList = "feed_id"))
public class FeedImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer number; // 순서

    @JoinColumn(name = "feed_id", nullable = false)
    @ManyToOne
    private Feed feed;
}
