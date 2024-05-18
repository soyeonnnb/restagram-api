package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
}
