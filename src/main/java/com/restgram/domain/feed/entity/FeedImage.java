package com.restgram.domain.feed.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url; // URL

    @Column(nullable = false)
    private Integer number; // 순서

    @JoinColumn(name = "feed_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed; // 피드

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        FeedImage that = (FeedImage) obj;

        return this.feed.equals(that.feed) &&
                this.number.equals(that.number)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.feed, this.number);
    }

}
