package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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

    // EmdAddress의 경우 DB에서 직접 생성하므로 ID값으로 비교한다.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        EmdAddress that = (EmdAddress) obj;
        return this.id != null && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return (int) (this.id % 100000000);
    }
}