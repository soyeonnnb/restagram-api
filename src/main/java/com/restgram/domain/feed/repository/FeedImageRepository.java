package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
    List<FeedImage> findAllByFeed(Feed feed);

    @EntityGraph(attributePaths = {"feed"})
    @Query("select fi from FeedImage fi where fi.feed.writer = :user and fi.number = :number order by fi.id desc")
    List<FeedImage> findByFeedWriterAndNumberOrderByIdDesc(User user, Integer number);

    @EntityGraph(attributePaths = {"feed"})
    @Query("select fi from FeedImage fi where fi.feed.store = :user and fi.feed.writer != :user and fi.number = :number order by fi.id desc")
    List<FeedImage> findByFeedStoreAndNumberOrderByIdDesc(User user, Integer number);
}
