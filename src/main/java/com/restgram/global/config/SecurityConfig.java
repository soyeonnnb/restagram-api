package com.restgram.global.config;

import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.service.CustomOAuth2UserService;
import com.restgram.global.handler.OAuth2AuthenticationFailureHandler;
import com.restgram.global.handler.OAuth2AuthenticationSuccessHandler;
import com.restgram.global.jwt.filter.JwtFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable) // 기본
                .authorizeHttpRequests(
                        authorize -> authorize
                                .dispatcherTypeMatchers(DispatcherType.ERROR).denyAll() // 인가 안되면 자체 시큐리티 타는데 -> 이거 막아주는 로직
                                .requestMatchers("/store/join", "/user/login", "/user/reissue", "/customer/join", "/login/oauth2/**").permitAll()
                                .requestMatchers("/user/logout", "/customer/info").authenticated()
                                .requestMatchers("/coupon", "/coupon/stop/**", "/coupon/finish").hasAuthority(UserType.STORE.toString())
                                .requestMatchers("/coupon/issue","/coupon/**").hasAuthority(UserType.CUSTOMER.toString())
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
