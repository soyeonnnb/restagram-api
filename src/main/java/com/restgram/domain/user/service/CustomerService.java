package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;

public interface CustomerService {
    LoginResponse getUserInfo(Long id);
    void join(CustomerJoinRequest req);
    void updateUserAddress(Long userId, Long addressId, Integer range);
    UserAddressListResponse getUserAddressList(Long userId);
}
