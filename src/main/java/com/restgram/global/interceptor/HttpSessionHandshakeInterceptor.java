package com.restgram.global.interceptor;

import com.restgram.global.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 추후 테스트
        log.info("WebSocket handshake interceptor ...");
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
//
////            attributes.put("token", token.getValue());
//        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
