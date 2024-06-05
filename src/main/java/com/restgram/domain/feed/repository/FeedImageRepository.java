package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
    List<FeedImage> findAllByFeed(Feed feed);

    @EntityGraph(attributePaths = {"feed"})
    @Query("select fi from FeedImage fi where fi.feed.writer = :user and fi.number = :number order by fi.id desc")
    List<FeedImage> findByFeedWriterAndNumberOrderByIdDesc(User user, Integer number, Pageable pageable);

    @EntityGraph(attributePaths = {"feed"})
    @Query("select fi from FeedImage fi where fi.feed.store = :user and fi.feed.writer != :user and fi.number = :number order by fi.id desc")
    List<FeedImage> findByFeedStoreAndNumberOrderByIdDesc(User user, Integer number, Pageable pageable);

    void deleteAllByFeed(Feed feed);

    @Modifying
    @Query(value = "INSERT INTO feed_image (created_at, updated_at, number, url, feed_id) VALUES (:createdAt, :updatedAt, :number, :url, :feedId)", nativeQuery = true)
    void insertFeedImage(@Param("createdAt") LocalDateTime createdAt,
                         @Param("updatedAt") LocalDateTime updatedAt,
                         @Param("number") int number,
                         @Param("url") String url,
                         @Param("feedId") int feedId);
}
