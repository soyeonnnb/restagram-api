package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void logout(HttpServletResponse response, String accessToken, String refreshToken);
    String reissue(HttpServletResponse response, String accessToken, String refreshToken);
    List<UserInfoResponse> searchUser(String query);
    FeedUserInfoResponse getFeedUser(Long myId, Long userId);
    void updatePassword(Long userId, UpdatePasswordRequest request);
    void updateNickname(Long userId, NicknameRequest request);
    UserProfileResponse updateProfileImage(Long userId, MultipartFile image);
    CheckResponse duplicateNickname(String query);
}
