package shop.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shop.config.JwtService;
import shop.shop.domain.entity.Member;
import shop.shop.domain.repository.member.MemberRepository;

import java.util.Optional;

import static shop.shop.controller.member.dto.request.MemberRequestDto.*;
import static shop.shop.controller.member.dto.response.MemberResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Long register(MemberRegisterRequest request) {
        Member member = request.toEntity();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
        return member.getId();
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
}
