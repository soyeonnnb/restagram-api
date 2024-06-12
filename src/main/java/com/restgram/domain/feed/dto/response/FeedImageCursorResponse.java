package com.restgram.domain.feed.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record FeedImageCursorResponse(

    Long cursorId,
    List<UserFeedImageResponse> images,
    boolean hasNext

) {

}
