package com.restgram.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date expiredAt; // 만료일

    @Column(unique = true, nullable = false)
    private String accessToken; // 액세스 토큰

    @Column(unique = true, nullable = false)
    private String refreshToken; // 리프레시 토큰

}