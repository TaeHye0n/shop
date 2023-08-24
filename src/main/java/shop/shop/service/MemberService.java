package shop.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shop.Role;
import shop.shop.component.SecurityContextUtil;
import shop.shop.config.JwtService;
import shop.shop.domain.entity.Member;
import shop.shop.domain.repository.member.MemberRepository;
import shop.shop.exception.CustomAccessDeniedException;

import java.util.Optional;

import static shop.shop.controller.member.dto.request.MemberRequestDto.*;
import static shop.shop.controller.member.dto.response.MemberResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextUtil securityContextUtil;

    @Transactional
    public Long register(MemberRegisterRequest request) {
        validateDuplicateMember(request.getEmail());
        Member member = request.toEntity();
        member.changeRole(Role.USER);
        member.encodePassword(passwordEncoder);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public AuthenticationResponse signIn(MemberSignInRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            Member member = (Member) authentication.getPrincipal(); // 인증된 사용자 정보 가져오기
            String token = jwtService.generateToken(member);

            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (UsernameNotFoundException ex) {
            // 인증 실패한 경우
            throw new BadCredentialsException("Authentication failed", ex);
        }
    }

    public MemberInfoResponse myInfo(Long memberId) throws CustomAccessDeniedException {
        Member member = securityContextUtil.getCurrentMember();
        if (member.getId().equals(memberId)) {
            return MemberInfoResponse.builder()
                    .address(member.getAddress())
                    .age(member.getAge())
                    .phone(member.getPhone())
                    .role(member.getRole())
                    .email(member.getEmail())
                    .name(member.getName())
                    .build();
        } else throw new CustomAccessDeniedException("Access Denied");
    }
}
