package shop.shop.domain.entity.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLaptop is a Querydsl query type for Laptop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLaptop extends EntityPathBase<Laptop> {

    private static final long serialVersionUID = 984670740L;

    public static final QLaptop laptop = new QLaptop("laptop");

    public final QItem _super = new QItem(this);

    //inherited
    public final StringPath brand = _super.brand;

    //inherited
    public final StringPath color = _super.color;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Float> inches = createNumber("inches", Float.class);

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final NumberPath<Integer> price = _super.price;

    //inherited
    public final NumberPath<Integer> stock = _super.stock;

    public QLaptop(String variable) {
        super(Laptop.class, forVariable(variable));
    }

    public QLaptop(Path<? extends Laptop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLaptop(PathMetadata metadata) {
        super(Laptop.class, metadata);
    }

}

