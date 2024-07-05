package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "siggAddress")
    private List<EmdAddress> emdAddressList; // 읍면동 엔티티 리스트

    // SiggAddress 경우 DB에서 직접 생성하므로 ID값으로 비교한다.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        SiggAddress that = (SiggAddress) obj;
        return this.id != null && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return (int) (this.id % 100000000);
    }
}
