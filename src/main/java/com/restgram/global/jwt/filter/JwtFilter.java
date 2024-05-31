package com.restgram.global.jwt.filter;

import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.restgram.global.exception.errorCode.JwtTokenErrorCode;
import com.restgram.global.jwt.response.JwtErrorResponseSender;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtErrorResponseSender jwtErrorResponseSender;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    @Override // 이 주소로 오는 건 토큰 없어도 됨.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/user/login") || path.equals("/store/join") || path.equals("/user/logout") || path.equals("/user/reissue") || path.equals("/customer/join") || path.startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Jwt Filter process ..");
        String accessToken = jwtTokenProvider.resolveToken(request, "access");

        // 만약 토큰이 존재하면 SecurityContextHolder에 권한을 생성하여 삽입
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken, response)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else return;
        } else {
            // 토큰이 필요한 요청에서 토큰이 없다면 리턴
            jwtErrorResponseSender.sendErrorResponse(response, JwtTokenErrorCode.DOES_NOT_EXIST_TOKEN);
            return;
        }
        filterChain.doFilter(request, response);
    }

}