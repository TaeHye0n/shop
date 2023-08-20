package shop.shop.domain.entity.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.enums.Quality;
import shop.shop.exception.NotEnoughStockException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stock;

    @Enumerated(EnumType.STRING)
    private Quality quality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Item(String name, int price, int stock, Quality quality) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quality = quality;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getItems().add(this);
    }

    public static Item item(Member member, String name, int price, int stock, Quality quality) {
        Item item = new Item();
        item.setMember(member);
        item.changeName(name);
        item.changePrice(price);
        item.changeStock(stock);
        item.changeQuality(quality);

        return item;
    }


    //비즈니스 로직
    public void removeStock(int count) {
        int restStock = stock - count;
        if (restStock < 0) throw new NotEnoughStockException("need more stock");
        this.stock = restStock;
    }

    public void addStock(int count) {
        this.stock += count;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeStock(int stock) {
        this.stock = stock;
    }

    public void changeQuality(Quality quality) {
        this.quality = quality;
    }
}
