package yuseteam.mealticketsystemwas.domain.oauthjwt.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import yuseteam.mealticketsystemwas.domain.oauthjwt.dto.CustomOAuth2UserDTO;
import yuseteam.mealticketsystemwas.domain.oauthjwt.jwt.JWTService;

import java.io.IOException;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTService jwtService;

    public CustomSuccessHandler(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2UserDTO principal = (CustomOAuth2UserDTO) authentication.getPrincipal();

        Long userId = principal.getId();
        String roleName = principal.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .orElse(null);

        String token = jwtService.createToken(userId, roleName);
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

    }
}
