package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
    
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);

    boolean existsByAccessTokenAndRefreshToken(String accessToken, String refreshToken);

    boolean existsByRefreshToken(String refreshToken);
}
