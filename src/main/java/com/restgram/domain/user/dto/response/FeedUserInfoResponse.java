package com.restgram.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedUserInfoResponse {
    private Long id;
    private String type;
    private String nickname;
    private String description;
    private String profileImage;
    private Integer feedNum;
    private Integer followingNum;
    private boolean isFollow;
}
