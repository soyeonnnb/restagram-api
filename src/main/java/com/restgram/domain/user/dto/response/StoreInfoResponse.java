package com.restgram.domain.user.dto.response;

import com.restgram.domain.address.dto.res.EmdAddressRes;
import com.restgram.domain.user.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoResponse {
    private Long id;
    private String profileImage;
    private String nickname;
    private String storeName;
    private String address;
    private String detailAddress;
    private String storePhone;

    public static StoreInfoResponse of(Store store) {
        return StoreInfoResponse.builder()
                .id(store.getId())
                .profileImage(store.getProfileImage())
                .nickname(store.getNickname())
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .detailAddress(store.getDetailAddress())
                .storePhone(store.getStorePhone())
                .build();
    }
}
