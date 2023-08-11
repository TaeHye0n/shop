package shop.shop.domain.entity.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("laptop")
public class Laptop extends Item {

    private float inches;
}
