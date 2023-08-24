package shop.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.exception.CustomAccessDeniedException;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> myInfo (
            @PathVariable Long memberId
    ) throws CustomAccessDeniedException {
        return ResponseEntity.ok(memberService.myInfo(memberId));
    }
}
