package com.restgram.domain.feed.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedQuerydslRepository;
import com.restgram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.restgram.domain.feed.entity.QFeed.feed;
import static com.restgram.domain.user.entity.QStore.store;

@RequiredArgsConstructor
public class FeedQuerydslRepositoryImpl implements FeedQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private final FeedImageRepository feedImageRepository;
    private final FeedLikeRepository feedLikeRepository;

    @Override
    public List<FeedResponse> findByIdLessThanAndWriterInOrderByIdDescQuerydsl(Long cursorId, List<User> userList, User loginUser) {
        List<Feed> feeds = queryFactory
                .selectFrom(feed)
                .where(feed.id.lt(cursorId)
                        .and(feed.writer.in(userList)))
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        // 가져온 feed ID 목록 추출
        List<Long> feedIds = feeds.stream()
                .map(Feed::getId)
                .collect(Collectors.toList());

        // 두 번째 쿼리: feed ID 목록을 이용해 연관 데이터 가져오기 (fetch join 사용 가능)
        List<Feed> results = queryFactory
                .selectFrom(feed)
                .leftJoin(feed.feedImageList).fetchJoin()
                .leftJoin(feed.store, store).fetchJoin()
                .where(feed.id.in(feedIds))
                .fetch();

        return results.stream()
                .map(f -> {
                    boolean isLike = feedLikeRepository.existsByFeedAndUser(f, loginUser);
                    return FeedResponse.of(f, isLike);
                })
                .collect(Collectors.toList());
    }
}
