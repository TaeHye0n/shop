package shop.shop.controller.item.dto.request;

import lombok.*;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.enums.Quality;
import shop.shop.domain.entity.item.Item;

public class ItemRequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemCreateRequest {
        private String name;
        private int price;
        private int stock;
        private Quality quality;

        public Item toEntity() {
            return Item.builder()
                    .name(name)
                    .price(price)
                    .stock(stock)
                    .quality(quality)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemUpdateRequest {
        private String name;
        private int price;
        private int stock;
        private Quality quality;
    }
}
