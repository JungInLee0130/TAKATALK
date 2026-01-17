package com.example.chat.global.security;

import com.example.chat.global.security.authentication.CustomAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/images/**", "/css/**", "/js/**", "/public/**").permitAll()     // 정적 리소스 permitAll
                        .requestMatchers("/api/v1/login/**", "/api/v1/users/test").permitAll()   // 로그인, 회원가입 permitAll
                        .requestMatchers("/login/**", "/user/signup", "/public/**").permitAll()   // 로그인, 회원가입 permitAll
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/mypage/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(form -> form     // 사용자정의 formLogin 사용
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/main", true)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login")
                        //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)   // IF_REQUIRED : 기본값
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/login")

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                           // post 요청
                        .logoutSuccessUrl("/login")                     // logout 성공시 이동할 url
                        .invalidateHttpSession(true)                    // http session 무효화
                        .deleteCookies("JSESSIONID")    // 로그아웃시 삭제할 쿠키이름
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/403")
                        .authenticationEntryPoint(((request, response, authException) -> {
                            // AJAX 요청인지 확인
                            String ajaxHeader = request.getHeader("X-Requested-With");
                            if ("XMLHttpRequest".equals(ajaxHeader)) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            } else {
                                response.sendRedirect("/login");
                            }
                        }))
                )
        ; // Http Basic 인증 비활성화

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));  // 모든 헤더 허용
        configuration.setAllowCredentials(true);    // 쿠키, 세션 허용시 필수

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 1. 공통 외벽 무시
    @Bean
    public WebSecurityCustomizer commonWebSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())   // 기본 정적 리소스 경로 외벽 차원에서 무시
                .requestMatchers("/favicon.ico", "/resources/**", "/error");
    }

    // 2. H2 콘솔은 켜져있을때만 외벽 무시
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer h2WebSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());  // h2 database 경로 외벽 차원에서 무시
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
