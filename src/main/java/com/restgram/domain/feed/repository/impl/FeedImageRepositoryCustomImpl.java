package com.restgram.domain.feed.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepositoryCustom;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.restgram.domain.feed.entity.QFeed.feed;
import static com.restgram.domain.feed.entity.QFeedImage.feedImage;

@RequiredArgsConstructor
public class FeedImageRepositoryCustomImpl implements FeedImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedImage> findByFeedWriterAndIdLessThanOrderByIdDesc(User user, Long cursorId) {
        List<Feed> feedList = queryFactory
                .selectFrom(feed)
                .where(feed.id.lt(cursorId)
                        .and(feed.writer.eq(user)))
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        // 가져온 feed ID 목록 추출
        List<Long> feedIds = feedList.stream()
                .map(Feed::getId)
                .collect(Collectors.toList());

        // 두 번째 쿼리: feed ID 목록을 이용해 첫번째 피드 이미지만 가져오기
        List<FeedImage> results = queryFactory
                .selectFrom(feedImage)
                .where(feedImage.feed.id.in(feedIds)
                        .and(feedImage.number.eq(0)))
                .fetch();

        // Feed ID 기준으로 정렬
        results.sort(Comparator.comparing((FeedImage feedImage) -> feedImage.getFeed().getId()).reversed());

        return results;
    }


    @Override
    public List<FeedImage> findByFeedStoreAndIdLessThanOrderByIdDesc(Store store, Long cursorId) {
        List<Feed> feedList = queryFactory
                .selectFrom(feed)
                .where(feed.id.lt(cursorId)
                        .and(feed.store.eq(store)))
                .orderBy(feed.id.desc())
                .limit(20)
                .fetch();

        // 가져온 feed ID 목록 추출
        List<Long> feedIds = feedList.stream()
                .map(Feed::getId)
                .collect(Collectors.toList());

        // 두 번째 쿼리: feed ID 목록을 이용해 첫번째 피드 이미지만 가져오기
        List<FeedImage> results = queryFactory
                .selectFrom(feedImage)
                .where(feedImage.feed.id.in(feedIds)
                        .and(feedImage.number.eq(0)))
                .fetch();

        // Feed ID 기준으로 정렬
        results.sort(Comparator.comparing((FeedImage feedImage) -> feedImage.getFeed().getId()).reversed());

        return results;
    }
}
