package com.restgram.domain.feed.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 피드 가게

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer; // 피드 작성자

    @Column(length = 2000)
    private String content; // 피드 내용

    @Column(length = 200)
    private String hashtag; // 해시태그

    @OneToMany(mappedBy = "feed")
    @Builder.Default
    List<FeedImage> feedImageList = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        Feed that = (Feed) obj;

        return this.store.equals(that.store) &&
                this.writer.equals(that.writer) &&
                this.getCreatedAt().equals(that.getCreatedAt())
                ;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.store, this.writer, this.getCreatedAt());
    }
}
