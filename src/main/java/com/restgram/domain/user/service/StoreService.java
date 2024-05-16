package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.JoinRequest;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface StoreService {
    void join(JoinRequest req);
    LoginResponse login(LoginRequest req, HttpServletResponse response);
}