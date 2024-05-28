package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "address_sigg", indexes = @Index(name = "idx_sigg_sido", columnList = "sido_id"))
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiggAddress {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "sido_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SidoAddress sidoAddress;
}
