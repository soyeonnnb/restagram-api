package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface StoreService {

  LoginResponse login(LoginRequest req, HttpServletResponse response);

  void join(StoreJoinRequest req);

  List<StoreInfoResponse> searchByName(String parameter);

  void updateStore(Long userId, UpdateStoreRequest request);

  CheckResponse duplicateEmail(String query);

  void updatePassword(Long userId, UpdatePasswordRequest request);

}
