package shop.shop.controller.member.dto.response;

import lombok.*;
import shop.shop.Role;
import shop.shop.domain.entity.embeddable.Address;

public class MemberResponseDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationResponse {
        private String token;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoResponse {
        private String email;
        private String name;
        private String phone;
        private int age;
        private Address address;
        private Role role;
    }
}
