package com.restgram.domain.follow.dto.response;

import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
