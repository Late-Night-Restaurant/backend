package com.backend.simya.global.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker   // Stomp 를 사용하기 위한 어노테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Stomp Endpoint 지정 및 개발 도메인을 origin 으로 설정
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://localhost:8080")   // "*"로 지정하면 에러 발생
                .withSockJS();
    }

    /**
     * 어플리케이션 내부에서 사용할 Path 지정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");  // 클라이언트에서 SEND 요청을 처리 (Spring docs 에는 "/topic"으로 나와 있음)
        registry.enableSimpleBroker("/sub");  // 해당 경로를 브로커로 등록하여 SUBSCRIBE 하는 클라이언트들에 메세지를 전달하는 간단한 작업 수행 (Spring docs 에는 "/queue"으로 나와 있음)
    }

    // enableStompBrokerRelay : SimpleBroker 의 기능과 외부 Message Broker(RabbitMQ, ActiveMQ 등)에 메세지를 전달하는 기능을 가짐
}
