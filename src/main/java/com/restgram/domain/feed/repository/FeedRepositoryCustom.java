package com.restgram.domain.feed.repository;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.user.entity.User;

import java.util.List;

public interface FeedRepositoryCustom {
    List<FeedResponse> findByIdLessThanAndWriterInOrderByIdDescQuerydsl(Long cursorId, List<User> userList, User loginUser);

    List<FeedResponse> searchByQueryAndEmdAddressList(String query, List<EmdAddress> emdAddressList, Long cursorId, User loginUser);

    List<FeedResponse> findByWriterAndIdLessOrGreaterThanIdDesc(User loginUser, User user, Long cursorId);
}
