package shop.shop.domain.repository.item;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static shop.shop.controller.item.dto.response.ItemResponseDto.*;
import static shop.shop.domain.entity.item.QItem.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ItemAllResponse> findAllMyItems(Long memberId) {
        return jpaQueryFactory
                .select(Projections.constructor(ItemAllResponse.class,
                        item.id,
                        item.name,
                        item.price,
                        item.stock,
                        item.quality
                        ))
                .from(item)
                .where(item.member.id.eq(memberId))
                .fetch();
    }
}
