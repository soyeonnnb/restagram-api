package com.restgram.domain.user.service;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest req, HttpServletResponse response);
    void logout(HttpServletResponse response, String accessToken, String refreshToken);
    void reissue(HttpServletResponse response, String accessToken, String refreshToken);
    List<UserInfoResponse> searchUser(String query);
}
