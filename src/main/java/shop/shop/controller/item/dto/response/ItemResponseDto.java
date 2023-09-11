package shop.shop.controller.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.shop.domain.entity.enums.Quality;

public class ItemResponseDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemAllResponse {
        private Long id;
        private String name;
        private int price;
        private int stock;
        private Quality quality;
    }
}
