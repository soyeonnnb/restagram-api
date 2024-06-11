package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
    boolean existsByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
    boolean existsByRefreshToken(String refreshToken);
}
