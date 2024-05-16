package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;

@Entity
@Builder
@Table(name = "address_sigg")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiggAddress {
    @Id
    private Long id;
    private String name;

    @JoinColumn(name = "sido_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SidoAddress sido;
}
