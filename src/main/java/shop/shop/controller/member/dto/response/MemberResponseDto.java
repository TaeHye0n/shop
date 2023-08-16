package shop.shop.controller.member.dto.response;

import lombok.*;

public class MemberResponseDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationResponse {
        private String token;
    }
}
