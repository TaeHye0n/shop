package shop.shop.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shop.shop.domain.entity.Member;
import shop.shop.exception.CustomAuthenticationException;

@Component
public class SecurityContextUtil {

    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Member) {
            return (Member) authentication.getPrincipal();
        } else throw new CustomAuthenticationException("Authentication failed");
    }
}
