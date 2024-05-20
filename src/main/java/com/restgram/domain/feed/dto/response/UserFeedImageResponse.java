package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedImageResponse {
    private Long id;
    private String imageUrl;
    private Long imageId;

    public static UserFeedImageResponse of(FeedImage feedImage) {
        return UserFeedImageResponse.builder()
                .id(feedImage.getFeed().getId())
                .imageId(feedImage.getId())
                .imageUrl(feedImage.getUrl())
                .build();
    }
}
