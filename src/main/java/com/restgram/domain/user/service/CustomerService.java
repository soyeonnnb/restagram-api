package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;

public interface CustomerService {
    LoginResponse getUserInfo(Long id);
    void join(CustomerJoinRequest req);
}
