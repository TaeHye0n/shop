package shop.shop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import shop.shop.domain.entity.embeddable.Address;
import shop.shop.domain.entity.enums.DeliveryStatus;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    @Builder
    public Delivery(Address address, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }
}
