package com.restgram.domain.follow.dto.response;

import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUserResponse {
    private Long id;
    private UserInfoResponse user;
    private boolean isFollowed;
    public static FollowUserResponse of(Long id, User user, boolean isFollowed) {
        return FollowUserResponse.builder()
                .id(id)
                .user(UserInfoResponse.of(user))
                .isFollowed(isFollowed)
                .build();
    }
}
