package shop.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.shop.service.MemberService;

import static shop.shop.controller.member.dto.request.MemberRequestDto.*;
import static shop.shop.controller.member.dto.response.MemberResponseDto.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> register(
            @RequestBody @Valid MemberRegisterRequest request
    ) {
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn (
            @RequestBody @Valid MemberSignInRequest request
    ) {
        return ResponseEntity.ok(memberService.signIn(request));
    }
}
