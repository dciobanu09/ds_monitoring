package com.example.ds_monitoring.controllers;

import com.example.ds_monitoring.rabbitmq.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final MessageSender messageSender;

    @Autowired
    public TestController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @GetMapping("/send")
    public String send(@RequestParam String message) {
        messageSender.sendMessage(message);
        return "Message sent to the queue: " + message;
    }
}
