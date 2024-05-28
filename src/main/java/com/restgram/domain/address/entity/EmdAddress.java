package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address_emd", indexes = @Index(name = "idx_emd_sigg", columnList = "sigg_id"))
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmdAddress {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "sigg_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress;
}
