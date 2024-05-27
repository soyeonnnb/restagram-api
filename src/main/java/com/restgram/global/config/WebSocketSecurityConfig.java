package com.restgram.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                // 추후 테스트
//                .simpTypeMatchers(SimpMessageType.CONNECT).authenticated()

                .simpTypeMatchers(SimpMessageType.CONNECT,
                        SimpMessageType.MESSAGE,
                        SimpMessageType.HEARTBEAT,
                        SimpMessageType.SUBSCRIBE,
                        SimpMessageType.DISCONNECT).permitAll()
                .simpDestMatchers("/ws/**", "/pub/**", "/sub/**").permitAll()
                .anyMessage().denyAll()
                .build();
    }

    @Bean("csrfChannelInterceptor") // for disable csrf
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        };
    }
}