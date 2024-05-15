package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmdAddress {
    @Id
    private Long id;

    @JoinColumn(name = "sigg_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress;
    private String name;
}
