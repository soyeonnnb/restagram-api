package com.restgram.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{

    @Column(nullable = false)
    private String uid;

    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;
}
