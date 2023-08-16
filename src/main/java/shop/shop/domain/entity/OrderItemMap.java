package shop.shop.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.shop.domain.entity.item.Item;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemMap {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private int orderPrice;

    public void setOrder(Order order) {
        this.order = order;
    }

    private void setItem(Item item) {
        this.item = item;
    }

    private void setCount(int count) {
        this.count = count;
    }

    private void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    //정적 팩토리 메서드
    public static OrderItemMap createOrderItemMap(Item item, int orderPrice, int count) {
        OrderItemMap orderItemMap = new OrderItemMap();
        orderItemMap.setItem(item);
        orderItemMap.setOrderPrice(orderPrice);
        orderItemMap.setCount(count);

        item.removeStock(count);

        return orderItemMap;
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
