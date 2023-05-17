package com.bit.bitcare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * configureMessageBroker
     * 클라이언트가 메시지를 구독할 endpoint 를 정의하는 메소드
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/send");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * ---------------------------------------------------------------------------
     * 2023.05.16
     *
     * registerStompEndpoints
     * connection 이 이뤄질때 CORS 를 허용하는 메소드
     */
    @Override
    // connection을 맺을때 CORS 허용합니다.
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/receive")
                // 스프링 5.3, 스프링부트 2.4 버전 부터 allowCredentials이 true인 경우 setAllowedOrigins 메서드에서
                // 와일드 카드 `'*'`을 사용하실 수 없습니다.
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
