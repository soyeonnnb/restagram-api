package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;

import java.util.List;

public interface FeedImageRepositoryCustom {
    List<FeedImage> findByFeedWriterAndIdLessThanOrderByIdDesc(User user, Long cursorId);

    List<FeedImage> findByFeedStoreAndIdLessThanOrderByIdDesc(Store store, Long cursorId);

}
