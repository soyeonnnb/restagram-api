package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.FeedImage;
import lombok.Builder;

@Builder
public record FeedImageResponse(

    Long id,
    String url,
    Integer number // 순서

) {

  public static FeedImageResponse of(FeedImage feedImage) {
    return FeedImageResponse.builder()
        .id(feedImage.getId())
        .url(feedImage.getUrl())
        .number(feedImage.getNumber())
        .build();
  }
}
