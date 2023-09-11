package shop.shop.domain.repository.item;

import java.util.List;

import static shop.shop.controller.item.dto.response.ItemResponseDto.*;

public interface ItemRepositoryCustom {
    List<ItemAllResponse> findAllMyItems(Long memberId);
}
