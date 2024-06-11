package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.FeedImage;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImageResponse {
    private Long id;
    private String url;
    private Integer number; // 순서

    public static FeedImageResponse of(FeedImage feedImage) {
        return FeedImageResponse.builder()
                .id(feedImage.getId())
                .url(feedImage.getUrl())
                .number(feedImage.getNumber())
                .build();
    }
}
