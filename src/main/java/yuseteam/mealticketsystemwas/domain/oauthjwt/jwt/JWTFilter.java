package yuseteam.mealticketsystemwas.domain.oauthjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import yuseteam.mealticketsystemwas.domain.oauthjwt.dto.UserDTO;
import yuseteam.mealticketsystemwas.domain.oauthjwt.entity.User;
import yuseteam.mealticketsystemwas.domain.oauthjwt.repository.UserRepository;

import java.io.IOException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        String authToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    authToken = cookie.getValue();
                    break;
                }
            }
        }

        if (authToken == null || authToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = authToken.replace("Bearer ", "");

        if (jwtService.isExpired(tokenValue)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired.");
            return;
        }

        Long userId = jwtService.parseUserId(tokenValue);

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found.");
            return;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        userDTO.setSocialname(user.getSocialname());

        List<GrantedAuthority> authorities;
        if (user.getRole() != null) {
            String rawRole = user.getRole().name();
            String normalizedRole = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;
            authorities = List.of(new SimpleGrantedAuthority(normalizedRole));
        } else {
            authorities = Collections.emptyList();
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDTO, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Cookie cookie = new Cookie("Authorization", authToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 60);
        response.addCookie(cookie);

        filterChain.doFilter(request, response);
    }
}