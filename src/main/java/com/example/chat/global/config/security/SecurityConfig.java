package com.example.chat.global.config.security;

import com.example.chat.global.config.security.authentication.CustomAuthenticationFailureHandler;
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

@Configuration
public class SecurityConfig {
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console())) // dev : csrf default로 사용. h2console만 해제
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // dev : h2 database permitAll
                        .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()     // 정적 리소스 permitAll
                        .requestMatchers("/login/**", "/user/signup").permitAll()   // 로그인, 회원가입 permitAll
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
                        .defaultSuccessUrl("/main")
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
                        .logoutUrl("/logout")   // post 요청
                        .logoutSuccessUrl("/login") // logout 성공시 이동할 url
                        .invalidateHttpSession(true)    // http session 무효화
                        .deleteCookies("JSESSIONID")    // 로그아웃시 삭제할 쿠키이름
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/403")
                )
        ; // Http Basic 인증 비활성화

        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    /*@Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }*/

    /*@Bean
    public UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.createUser(user);

        return jdbcUserDetailsManager;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
