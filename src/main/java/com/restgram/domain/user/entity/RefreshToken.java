package com.restgram.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        RefreshToken that = (RefreshToken) obj;

        return this.accessToken.equals(that.accessToken) &&
                this.refreshToken.equals(that.refreshToken)
                ;
    }

    @Override
    public int hashCode() {
        String str = new StringBuilder()
                .append(this.accessToken)
                .append("_")
                .append(this.refreshToken)
                .toString();
        return str.hashCode();
    }
}