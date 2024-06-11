package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedResponse {
    private Long id;
    private String content;
    private UserInfoResponse user; // 작성자 정보
    private StoreInfoResponse store; // 스토어 정보
    private List<FeedImageResponse> images;
    private LocalDateTime time;
    private Boolean isLike;

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
