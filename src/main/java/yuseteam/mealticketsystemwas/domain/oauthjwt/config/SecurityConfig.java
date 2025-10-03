package yuseteam.mealticketsystemwas.domain.oauthjwt.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import yuseteam.mealticketsystemwas.domain.oauthjwt.jwt.JWTFilter;
import yuseteam.mealticketsystemwas.domain.oauthjwt.jwt.JWTService;
import yuseteam.mealticketsystemwas.domain.oauthjwt.oauth2.CustomSuccessHandler;
import yuseteam.mealticketsystemwas.domain.oauthjwt.repository.UserRepository;
import yuseteam.mealticketsystemwas.domain.oauthjwt.service.CustomOAuth2UserService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error");
    }

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTService jwtService, UserRepository userRepository) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }

                }))
                .addFilterBefore(new JWTFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/error",
                                "/api/auth/signin",
                                "/api/auth/signup",
                                "/api/menus/**",
                                "/api/restaurants",
                                "/api/orders/**",
                                "/api/admin/**",
                                "/api/auth/initial-setup"
                        ).permitAll() //지금 임시로 열어두는것, 나중에 지우고 위에것으로 사용할 것.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setContentType("application/json;charset=UTF-8");
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"로그인이 필요합니다.\"}");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setContentType("application/json;charset=UTF-8");
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"권한이 없습니다.\"}");
//                        })
//                );
        return http.build();
    }

}