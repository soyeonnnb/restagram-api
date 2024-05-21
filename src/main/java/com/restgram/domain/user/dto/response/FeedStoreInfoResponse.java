package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedStoreInfoResponse extends FeedUserInfoResponse {
//    private EmdAddressRes emdAddress;
    private String address;
    private String detailAddress;
    private String storeName;
    private String storePhone;
    private Integer reviewNum;
    private Integer couponNum;

    public static FeedStoreInfoResponse of(Store store, Integer feedNum, Integer followingNum, Integer reviewNum, Integer couponNum, boolean isFollow) {
        return FeedStoreInfoResponse.builder()
                .id(store.getId())
                .type(store.getType())
                .nickname(store.getNickname())
                .description(store.getDescription())
                .profileImage(store.getProfileImage())
                .feedNum(feedNum)
                .followingNum(followingNum)
                .isFollow(isFollow)
//                .emdAddress(EmdAddressRes.of(store.getEmdAddress()))
                .address(store.getAddress())
                .detailAddress(store.getDetailAddress())
                .storeName(store.getStoreName())
                .storePhone(store.getStorePhone())
                .reviewNum(reviewNum)
                .couponNum(couponNum)
                .build();
    }
}