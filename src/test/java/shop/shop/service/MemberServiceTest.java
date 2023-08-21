package shop.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import shop.shop.config.JwtService;
import shop.shop.domain.entity.Member;
import shop.shop.domain.repository.member.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static shop.shop.controller.member.dto.request.MemberRequestDto.*;
import static shop.shop.controller.member.dto.response.MemberResponseDto.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Unit Test")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("회원가입")
    void 회원가입() throws Exception {
        //given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("test123");
        Member member = request.toEntity();
        ReflectionTestUtils.setField(member, "id", 1L);

        //when
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        Long memberId = memberService.register(request);


        //then
        assertThat(memberId).isEqualTo(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("중복회원감지")
    void 중복회원감지() throws Exception {
        //given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("test123");
        Member member = request.toEntity();
        ReflectionTestUtils.setField(member, "id", 1L);

        //when
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(member));

        //then
        assertThrows(IllegalStateException.class, () -> memberService.register(request));
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    @DisplayName("로그인")
    void 로그인() throws Exception {
        //given
        String email = "test@test.com";
        String password = "test123";
        String token = "testToken";

        MemberSignInRequest request = MemberSignInRequest.builder()
                .email(email)
                .password(password)
                .build();

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        //when
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(member, password));
        when(jwtService.generateToken(member)).thenReturn(token);
        AuthenticationResponse response = memberService.signIn(request);

        //then
        assertThat(response.getToken()).isEqualTo(token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(member);
    }

    @Test
    @DisplayName("로그인 실패")
    void 로그인실패() throws Exception {
        //given
        String email = "test@test.com";
        String password = "test123";

        MemberSignInRequest request = MemberSignInRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when
        when(authenticationManager.authenticate(any()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        //then
        assertThrows(BadCredentialsException.class, () -> memberService.signIn(request));
    }
}