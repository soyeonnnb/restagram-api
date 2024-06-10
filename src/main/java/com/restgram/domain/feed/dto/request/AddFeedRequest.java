package com.restgram.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddFeedRequest {
    private Long storeId;
    private String content;
}
