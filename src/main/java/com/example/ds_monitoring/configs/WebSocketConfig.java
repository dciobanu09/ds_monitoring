package com.example.ds_monitoring.configs;

import com.example.ds_monitoring.configs.handlers.WSHandler;
import com.example.ds_monitoring.configs.interceptors.UserHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS()
                .setInterceptors(new UserHandshakeInterceptor());
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketHandler(), "/ws")
//                .setAllowedOrigins("*") // Set the allowed origins as per your requirement
//                .addInterceptors(new UserHandshakeInterceptor()); // Add your custom interceptor
//    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WSHandler();
    }
}
