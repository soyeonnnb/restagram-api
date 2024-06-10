package com.restgram.domain.feed.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_feed_writer", columnList = "user_id"))
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    private String content;

    @OneToMany(mappedBy = "feed")
    List<FeedImage> feedImageList = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }
}
