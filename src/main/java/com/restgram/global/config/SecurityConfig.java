package com.restgram.global.config;

import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.service.impl.CustomOAuth2UserService;
import com.restgram.global.handler.OAuth2AuthenticationFailureHandler;
import com.restgram.global.handler.OAuth2AuthenticationSuccessHandler;
import com.restgram.global.jwt.filter.JwtFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 설정
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 직접 빈 주입
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                // 세션 사용 X
                .sessionManagement(
                        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable) // 기본
                .authorizeHttpRequests(
                        authorize -> authorize
                                .dispatcherTypeMatchers(DispatcherType.ERROR)
                                .permitAll() // 인가 안되면 자체 시큐리티 타는데 -> 이거 막아주는 로직
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/api/v1/store/join", "/api/v1/store/login",
                                        "/api/v1/user/reissue", "/api/v1/customer/join", "/api/v1/user/logout",
                                        "/api/v1/login/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/store/password").hasAuthority(UserType.STORE.getName())
                                .requestMatchers("/api/v1/customer/info", "/api/v1/customer/store", "/api/v1/customer/store/**").authenticated()
                                .requestMatchers("/api/v1/address/**").authenticated()
                                .requestMatchers("/api/v1/coupon", "/api/v1/coupon/stop/**",
                                        "/api/v1/coupon/finish").hasAuthority(UserType.STORE.getName())
                                .requestMatchers("/api/v1/coupon/issue", "/api/v1/coupon/**")
                                .hasAuthority(UserType.CUSTOMER.getName())
                                .requestMatchers("/api/v1/feed/**").authenticated()
                                .requestMatchers("/api/v1/follow/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/reservation").authenticated()
                                .requestMatchers("/api/v1/reservation", "/api/v1/reservation/customer")
                                .hasAuthority(UserType.CUSTOMER.getName())
                                .requestMatchers("/api/v1/reservation/store").hasAuthority(UserType.STORE.getName())
                                .requestMatchers("/api/v1/reservation/form").hasAuthority(UserType.STORE.getName())
                                .requestMatchers(HttpMethod.GET, "/api/v1/reservation/form/**")
                                .hasAuthority(UserType.CUSTOMER.getName())
                                .requestMatchers("/api/v1/reservation/**").authenticated()
                                .requestMatchers("/api/v1/notification/**").authenticated()
                                .requestMatchers("/api/v1/customer/calender/agree")
                                .hasAuthority(UserType.CUSTOMER.getName())
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login((oauth2) -> {
                    oauth2
                            .userInfoEndpoint((endpoint) -> endpoint.userService(customOAuth2UserService));
                    oauth2.successHandler(oAuth2AuthenticationSuccessHandler);
                    oauth2.failureHandler(oAuth2AuthenticationFailureHandler);
                })
        ;

        return http.build();
    }
}
