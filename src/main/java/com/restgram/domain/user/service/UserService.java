package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.FeedUserInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  void logout(HttpServletResponse response, String accessToken, String refreshToken);

  void reissue(HttpServletResponse response, String accessToken, String refreshToken);

  List<UserInfoResponse> searchUser(String query);

  FeedUserInfoResponse getFeedUser(Long myId, Long userId);

  void updatePassword(Long userId, UpdatePasswordRequest request);

  void updateNickname(Long userId, NicknameRequest request);

  void updateProfileImage(Long userId, MultipartFile image);

  CheckResponse duplicateNickname(String query);
}
