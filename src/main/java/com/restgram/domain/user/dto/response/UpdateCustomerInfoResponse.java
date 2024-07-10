package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Customer;
import lombok.Builder;

@Builder
public record UpdateCustomerInfoResponse(
        String description,
        String phone
) {
    public static UpdateCustomerInfoResponse of(Customer customer) {

        return UpdateCustomerInfoResponse.builder()
                .description(customer.getDescription())
                .phone(customer.getPhone())
                .build();
    }
}
