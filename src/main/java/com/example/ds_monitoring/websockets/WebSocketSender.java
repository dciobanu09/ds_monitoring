package com.example.ds_monitoring.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WebSocketSender {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    public void sendMessageToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/messages/" + userId, message);
    }
}
