package shop.shop.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.shop.domain.entity.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemMap> orderItemMaps = new ArrayList<>();

    //==연관관계 메서드==//
    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    private void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    private void addOrderItemMap(OrderItemMap orderItemMap) {
        orderItemMaps.add(orderItemMap);
        orderItemMap.setOrder(this);
    }

    public void orderComplete() {
        this.orderStatus = orderStatus.ORDER;
    }

    //정적 팩토리 메서드
    public static Order createOrder(Member member, Delivery delivery, List<OrderItemMap> orderItemMaps) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        orderItemMaps.stream().forEach(order::addOrderItemMap);

        order.orderComplete();
        return order;
    }

    public int getTotalPrice() {
        return orderItemMaps.stream()
                .mapToInt(OrderItemMap::getTotalPrice)
                .sum();
    }

}
