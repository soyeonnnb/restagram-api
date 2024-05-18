package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedLike;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    boolean existsByFeedAndUser(Feed feed, User user);
    @EntityGraph(attributePaths = {"feed", "feed.writer", "feed.store", "feed.store.address", "feed.store.address.siggAddress", "feed.store.address.siggAddress.sidoAddress"})
    List<FeedLike> findAlllByUserOrderByCreatedAtDesc(User user);
    void deleteByFeedAndUser(Feed feed, User user);
}
