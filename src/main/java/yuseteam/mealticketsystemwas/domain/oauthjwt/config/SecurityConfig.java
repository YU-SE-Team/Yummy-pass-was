package yuseteam.mealticketsystemwas.domain.oauthjwt.config;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
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

        // 로그인이 필요한 요청이 들어왔을 때 기본적으로 뜨는 /login 페이지를 제거하고
        // 대신 OAuth2 제공자 인증 시작 URL로 바로 리디렉트하도록 설정합니다.
        // 여기서는 카카오를 기본 제공자로 지정하여 /oauth2/authorization/kakao 로 리디렉트합니다.
        final String defaultOAuth2LoginUrl = "/api/auth/signup";

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
                        // 기본 /login 페이지 대신 바로 카카오 OAuth 인증으로 이동하도록 설정
                        .loginPage(defaultOAuth2LoginUrl)
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
                                "/api/auth/initial-setup",
                                "/api/tickets/**"
                        ).permitAll() //지금 임시로 열어두는것, 나중에 지우고 위에것으로 사용할 것.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 명시적으로 form login과 httpBasic을 비활성화하여 기본 로그인 폼이 제공되지 않도록 합니다.
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 인증이 필요한 요청이 왔을 때 OAuth2 인증 시작 URL로 리다이렉트하도록 설정
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(defaultOAuth2LoginUrl))
                );

        return http.build();
    }

}