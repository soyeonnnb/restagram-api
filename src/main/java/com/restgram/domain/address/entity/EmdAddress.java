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
    private Long id;
    private String name;

    @JoinColumn(name = "sigg_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress;
}
