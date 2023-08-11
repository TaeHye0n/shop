package shop.shop.domain.entity.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhone is a Querydsl query type for Phone
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhone extends EntityPathBase<Phone> {

    private static final long serialVersionUID = -657071628L;

    public static final QPhone phone = new QPhone("phone");

    public final QItem _super = new QItem(this);

    public final NumberPath<Integer> batteryTime = createNumber("batteryTime", Integer.class);

    //inherited
    public final StringPath brand = _super.brand;

    //inherited
    public final StringPath color = _super.color;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final NumberPath<Integer> price = _super.price;

    //inherited
    public final NumberPath<Integer> stock = _super.stock;

    public QPhone(String variable) {
        super(Phone.class, forVariable(variable));
    }

    public QPhone(Path<? extends Phone> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhone(PathMetadata metadata) {
        super(Phone.class, metadata);
    }

}

