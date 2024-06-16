package com.restgram.domain.feed.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedRepositoryCustom;
import com.restgram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.restgram.domain.feed.entity.QFeed.feed;
import static com.restgram.domain.user.entity.QStore.store;

@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final FeedLikeRepository feedLikeRepository;

    @Override
    public List<FeedResponse> findByIdLessThanAndWriterInOrderByIdDescQuerydsl(Long cursorId, List<User> userList, User loginUser) {
        BooleanBuilder builder = new BooleanBuilder();

        // 커서 ID
        if (cursorId != null) {
            builder.and(feed.id.lt(cursorId));
        }
        builder.and(feed.writer.in(userList));

        List<Feed> feeds = queryFactory
                .selectFrom(feed)
                .where(builder)
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        return feedToFeedResponse(feeds, loginUser);
    }

    @Override
    public List<FeedResponse> searchByQueryAndEmdAddressList(String query, List<EmdAddress> emdAddressList, Long cursorId, User loginUser) {
        BooleanBuilder builder = new BooleanBuilder();

        // 커서 ID
        if (cursorId != null) {
            builder.and(feed.id.lt(cursorId));
        }

        // 쿼리
        if (query != null && !query.isEmpty()) {
            builder.and(feed.hashtag.like(query));
        }

        // 읍면동 리스트
        if (emdAddressList != null && !emdAddressList.isEmpty()) {
            builder.and(feed.store.emdAddress.in(emdAddressList));
        }

        List<Feed> feeds = queryFactory
                .selectFrom(feed)
                .where(builder)
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        return feedToFeedResponse(feeds, loginUser);
    }

    @Override
    public List<FeedResponse> findByWriterAndIdLessOrGreaterThanIdDesc(User loginUser, User user, Long cursorId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cursorId != null) {
            builder.and(feed.id.lt(cursorId));

        }
        builder.and(feed.writer.eq(user));

        List<Feed> feeds = queryFactory
                .selectFrom(feed)
                .where(builder)
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        return feedToFeedResponse(feeds, loginUser);
    }

    private List<FeedResponse> feedToFeedResponse(List<Feed> feeds, User loginUser) {

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
                .orderBy(feed.id.desc())
                .fetch();

        return results.stream()
                .map(f -> {
                    boolean isLike = feedLikeRepository.existsByFeedAndUser(f, loginUser);
                    return FeedResponse.of(f, isLike);
                })
                .collect(Collectors.toList());
    }
}
