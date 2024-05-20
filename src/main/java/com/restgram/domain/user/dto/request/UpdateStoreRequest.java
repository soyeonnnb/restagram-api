package com.restgram.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateStoreRequest {
    private String description;
    private String phone;
    private String storeName;
    private String storePhone;
    private Long bcode;
    private String address;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
}
