package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findAllByWriterInOrWriterOrderByCreatedAtDesc(List<User> userList, User writer);
}
