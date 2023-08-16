package shop.shop.controller.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import shop.shop.domain.entity.Member;
import shop.shop.domain.entity.embeddable.Address;

public class MemberRequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRegisterRequest {

        @NotNull
        private String email;
        private String name;
        private String password;
        private String phone;
        private int age;
        private Address address;

        @Builder
        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .name(name)
                    .password(password)
                    .phone(phone)
                    .age(age)
                    .address(address)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSignInRequest {
        private String email;
        private String password;
    }
}
