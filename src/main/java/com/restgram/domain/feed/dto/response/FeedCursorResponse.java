package com.restgram.domain.feed.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedCursorResponse {
    private Long cursorId;
    private List<FeedResponse> feeds;
    private boolean hasNext;
}
