package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void logout(HttpServletResponse response, String accessToken, String refreshToken);
    void reissue(HttpServletResponse response, String accessToken, String refreshToken);
}
