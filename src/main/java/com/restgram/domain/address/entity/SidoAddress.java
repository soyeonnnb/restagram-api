package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "sidoAddress")
    private List<SiggAddress> siggAddressList; // 시군구 엔티티 리스트
}
