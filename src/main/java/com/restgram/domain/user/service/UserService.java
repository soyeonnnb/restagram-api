package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.FeedUserInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoCursorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void logout(HttpServletResponse response, String accessToken, String refreshToken);

    void reissue(HttpServletResponse response, String accessToken, String refreshToken);

    UserInfoCursorResponse searchUser(Long cursorId, String query);

    FeedUserInfoResponse getFeedUser(Long myId, Long userId);

    void updateNickname(Long userId, NicknameRequest request);

    void updateProfileImage(Long userId, MultipartFile image);

    CheckResponse duplicateNickname(String query);
}
