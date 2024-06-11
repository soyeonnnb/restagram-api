package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.FeedImage;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
