package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;
    private String profileImage;
    private String phone;
    private String description;
    private String nickname;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Transient
    public String getType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

}
