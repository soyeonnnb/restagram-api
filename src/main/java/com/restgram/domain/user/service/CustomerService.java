package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;

public interface CustomerService {
    LoginResponse getUserInfo(Long userId);
    void updateUserAddress(Long userId, Long addressId, Integer range);
    void updateCustomer(Long userId, UpdateCustomerRequest request);
    UserAddressListResponse getUserAddressList(Long userId);
}
