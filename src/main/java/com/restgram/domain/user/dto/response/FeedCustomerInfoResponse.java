package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCustomerInfoResponse extends FeedUserInfoResponse {
    private Integer followerNum;
    public static FeedCustomerInfoResponse of(Customer customer, Integer feedNum, Integer followerNum, Integer followingNum, boolean isFollow) {
        return FeedCustomerInfoResponse.builder()
                .id(customer.getId())
                .type(customer.getType())
                .nickname(customer.getNickname())
                .description(customer.getDescription())
                .profileImage(customer.getProfileImage())
                .feedNum(feedNum)
                .followerNum(followerNum)
                .followingNum(followingNum)
                .isFollow(isFollow)
                .build();
    }
}
