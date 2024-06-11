package com.restgram.domain.user.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 성명
    private String profileImage; // 프로필 이미지
    private String phone; // 전화번호
    private String description; // 설명

    @Column(nullable = false, unique = true, length = 20)
    private String nickname; // 닉네임
    private String password; // 비밀번호

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    public String getType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String url) {
        this.profileImage = url;
    }

}
