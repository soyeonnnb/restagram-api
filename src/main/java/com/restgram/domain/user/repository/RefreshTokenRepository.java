package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
    Boolean existsByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
}
