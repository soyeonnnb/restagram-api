package com.restgram.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateCustomerRequest {
    private String description;
    private String phone;
}
