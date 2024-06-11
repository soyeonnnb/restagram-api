package com.restgram.domain.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "address_sido")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SidoAddress {
    @Id
    private Long id; // 시도 ID

    @Column(nullable = false, length = 30)
    private String name; // 시도 이름
}
