package com.restgram.domain.feed.dto.response;

import com.restgram.domain.feed.entity.FeedImage;
import lombok.Builder;

@Builder
public record UserFeedImageResponse(

    Long id,
    String imageUrl,
    Long imageId

) {

  public static UserFeedImageResponse of(FeedImage feedImage) {
    return UserFeedImageResponse.builder()
        .id(feedImage.getFeed().getId())
        .imageId(feedImage.getId())
        .imageUrl(feedImage.getUrl())
        .build();
  }
}
