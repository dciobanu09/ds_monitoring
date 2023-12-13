package com.example.ds_monitoring.controllers;

import com.example.ds_monitoring.rabbitmq.MessageSender;
import com.example.ds_monitoring.websockets.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/communication")
public class CommunicationController {
    private final MessageSender messageSender;

    @Autowired
    public CommunicationController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
