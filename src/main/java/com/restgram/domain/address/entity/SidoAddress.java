package com.restgram.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

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

    // SidoAddress 경우 DB에서 직접 생성하므로 ID값으로 비교한다.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        SidoAddress that = (SidoAddress) obj;
        return this.id != null && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return (int) (this.id % 100000000);
    }
}
