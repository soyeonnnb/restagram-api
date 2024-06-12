package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.user.entity.User;

import java.util.List;

public interface FeedQuerydslRepository {
    List<FeedResponse> findByIdLessThanAndWriterInOrderByIdDescQuerydsl(Long cursorId, List<User> userList, User loginUser);
}
