package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "address_sigg")
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiggAddress {
    @Id
    private Long id; // 시군구 ID

    @Column(nullable = false, length = 30)
    private String name; // 시군구명

    @JoinColumn(name = "sido_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SidoAddress sidoAddress; // 시군구가 포함된 엔티티
}
