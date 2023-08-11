package shop.shop.domain.entity.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("phone")
public class Phone extends Item {

    private int batteryTime; //분
}
