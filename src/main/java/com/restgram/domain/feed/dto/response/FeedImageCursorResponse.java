package com.restgram.domain.feed.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedImageCursorResponse {
    private Long cursorId;
    private List<UserFeedImageResponse> images;
    private boolean hasNext;
}
