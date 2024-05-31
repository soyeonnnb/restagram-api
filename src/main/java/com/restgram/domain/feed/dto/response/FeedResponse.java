package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {
    private Long id;
    private String content;
    private UserInfoResponse user; // 작성자 정보
    private StoreInfoResponse store; // 스토어 정보
    private List<FeedImageResponse> images;
    private LocalDateTime time;
    private Boolean isLike;

    public static FeedResponse of(Feed feed, List<FeedImage> images, boolean isLike) {
        List<FeedImageResponse> feedImageResponsesList = new ArrayList<>();
        for(FeedImage feedImage : images) feedImageResponsesList.add(FeedImageResponse.of(feedImage));
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
