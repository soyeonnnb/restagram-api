package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address_emd")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmdAddress {
    @Id
    private Long id; // 읍면동 ID

    @Column(nullable = false, length = 30)
    private String name; // 읍면동 이름

    @JoinColumn(name = "sigg_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress; // 읍면동이 포함된 엔티티
}
