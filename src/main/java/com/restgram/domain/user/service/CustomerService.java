package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.global.entity.PaginationResponse;

public interface CustomerService {
    LoginResponse getUserInfo(Long userId);

    void updateCustomer(Long userId, UpdateCustomerRequest request);

    StoreInfoResponse getStoreInfo(Long storeId);

    PaginationResponse getStoreList(Long cursorId, String query);
}
