package com.restgram.global.interceptor;

import com.restgram.global.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider tokenProvider;

    // websocket을 통해 들어온 요청이 처리 되기전 실행됨
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("token 유효성 확인");
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // websocket 연결시 헤더의 jwt token 유효성 검증
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//            String accessToken = accessor.getFirstNativeHeader("Authorization");
//            // 토큰 검증
//            if (StringUtils.hasText(accessToken)) {
//                try {
//                    // 검증 성공한 경우 Authentication 객체 생성
//                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    accessor.setUser(authentication); // accessor에 등록
//                } catch (Exception e) {
//                    // 검증 실패 시 에러 로깅 등을 처리할 수 있음
//                    log.error("WebSocket Connection Authentication Error: {}", e.getMessage());
//                    throw new AccessDeniedException("Access Denied");
//                }
//            } else {
//                // 토큰이 올바르지 않은 경우
//                log.error("WebSocket Connection Authentication Error: Invalid Token Format");
//                throw new AccessDeniedException("Access Denied");
//            }
//        }
        return message;
    }


}