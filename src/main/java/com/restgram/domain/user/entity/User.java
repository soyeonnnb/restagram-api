package com.restgram.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User {

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

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime joinedAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Transient
  public String getType() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
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
