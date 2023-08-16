package shop.shop.controller.order.dto.request;

import lombok.*;

import java.util.List;

public class OrderRequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderCreateRequest {
        private Long memberId;

        private List<ItemAndCount> itemAndCounts;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ItemAndCount {
            private Long ItemId;
            private int count;
        }
    }
}
