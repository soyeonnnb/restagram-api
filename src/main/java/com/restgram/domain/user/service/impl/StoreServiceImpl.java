package com.restgram.domain.user.service.impl;


import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.domain.user.service.StoreService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final EmdAddressRepository emdAddressRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  // 회원가입
  @Override
  @Transactional
  public void join(StoreJoinRequest req) {
    // 중복 이메일 확인
    if (storeRepository.existsByEmail(req.getEmail())) {
      throw new RestApiException(UserErrorCode.EMAIL_DUPLICATED,
          "이메일이 중복되었습니다. [이메일=" + req.getEmail() + "]");
    }

    // 중복 닉네임 확인
    if (userRepository.existsByNickname(req.getNickname())) {
      throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED,
          "닉네임이 중복되었습니다. [닉네임=" + req.getNickname() + "]");
    }

    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(req.getPassword());

    // 주소 가져오기
    EmdAddress emdAddress = emdAddressRepository.findById(req.getBcode()).orElseThrow(
        () -> new RestApiException(AddressErrorCode.INVALID_BCODE,
            "주소 BODE가 유효하지 않습니다. [BCODE=" + req.getBcode() + "]"));

    // 저장
    storeRepository.save(req.of(emdAddress, encodedPassword));
  }

  @Override
  @Transactional(readOnly = true)
  public List<StoreInfoResponse> searchByName(String parameter) {
    List<Store> storeList = storeRepository.findAllByStoreNameOrNicknameContaining(parameter);

    return storeList.stream().map(StoreInfoResponse::of).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateStore(Long userId, UpdateStoreRequest request) {
    Store store = storeRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
    store.updateDescription(request.getDescription());
    store.updatePhone(request.getPhone());
    store.updateStoreInfo(request, emdAddressRepository.findById(request.getBcode())
        .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_BCODE)));
  }

  // 로그인
  @Override
  @Transactional
  public LoginResponse login(LoginRequest req, HttpServletResponse response) {
    Store store = storeRepository.findByEmail(req.getEmail())
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
            "로그인 시도 이메일이 잘못되었습니다. [이메일=" + req.getEmail() + "]"));
    // 비밀번호 확인
    if (!passwordEncoder.matches(req.getPassword(), store.getPassword())) {
      throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH,
          "비밀번호가 일치하지 않습니다. [로그인 요청 사용자ID=" + store.getId() + "]");
    }
    tokenProvider.createTokens(store.getId(), UserType.STORE.getName(), response);

    return LoginResponse.of(store);
  }


  @Override
  @Transactional(readOnly = true)
  public CheckResponse duplicateEmail(String email) {
    return CheckResponse.builder()
        .check(storeRepository.existsByEmail(email))
        .build();
  }
}
