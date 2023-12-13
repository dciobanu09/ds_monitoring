package com.example.ds_monitoring.configs.interceptors;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

public class UserHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String userIdString = request.getURI().getQuery().split("user=")[1];
        if (userIdString != null && !userIdString.isEmpty()) {
            try {
                UUID userId = UUID.fromString(userIdString);
                attributes.put("userId", userId);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid UUID in header");
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
