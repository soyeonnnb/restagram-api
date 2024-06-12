package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record FeedResponse(

        Long id,
        String content,
        UserInfoResponse user, // 작성자 정보
        StoreInfoResponse store, // 스토어 정보
        List<FeedImageResponse> images,
        LocalDateTime time,
        Boolean isLike

) {

    public static FeedResponse of(Feed feed, boolean isLike) {
        List<FeedImageResponse> feedImageResponsesList = feed.getFeedImageList().stream()
                .map(image -> FeedImageResponse.of(image))
                .collect(Collectors.toList());

        return FeedResponse.builder()
                .id(feed.getId())
                .content(feed.getContent())
                .time(feed.getCreatedAt())
                .user(UserInfoResponse.of(feed.getWriter()))
                .store(StoreInfoResponse.of(feed.getStore()))
                .images(feedImageResponsesList)
                .isLike(isLike)
                .build();
    }

    public static FeedResponse of(Feed feed, List<FeedImage> images, boolean isLike) {
        List<FeedImageResponse> feedImageResponsesList = images.stream()
                .map(image -> FeedImageResponse.of(image))
                .collect(Collectors.toList());

        return FeedResponse.builder()
                .id(feed.getId())
                .content(feed.getContent())
                .time(feed.getCreatedAt())
                .user(UserInfoResponse.of(feed.getWriter()))
                .store(StoreInfoResponse.of(feed.getStore()))
                .images(feedImageResponsesList)
                .isLike(isLike)
                .build();
    }
}
