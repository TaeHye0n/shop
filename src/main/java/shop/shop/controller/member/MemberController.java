package shop.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shop.common.response.Response;
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
    public ResponseEntity<Response<Long>> register(
            @RequestBody @Valid MemberRegisterRequest request
    ) {
        return ResponseEntity.ok(
                Response.of(memberService.register(request),
                "회원가입 완료")
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<Response<AuthenticationResponse>> signIn (
            @RequestBody @Valid MemberSignInRequest request
    ) {
        return ResponseEntity.ok(
                Response.of(memberService.signIn(request),
                "로그인 성공")
                );
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Response<MemberInfoResponse>> myInfo (
            @PathVariable Long memberId
    ) throws CustomAccessDeniedException {
        return ResponseEntity.ok(
                Response.of(memberService.myInfo(memberId),
                "나의 정보 조회 완료")
        );
    }
}
