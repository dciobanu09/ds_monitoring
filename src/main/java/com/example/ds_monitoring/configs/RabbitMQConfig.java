package com.example.ds_monitoring.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    @Bean
    public Queue queue() {
        return new Queue("testqueue", true);
    }

    @Bean
    public Queue deviceUpdatesQueue() {
        return new Queue("deviceUpdatesQueue", true);
    } // sa pun extra ala

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("testexchange");
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("appRoutingKey");
    }

    @Bean
    public Binding bindingDeviceUpdatesQueue() {
        return BindingBuilder.bind(deviceUpdatesQueue()).to(exchange()).with("deviceUpdatesRoutingKey");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2JsonMessageConverter());
        rabbitTemplate.setConfirmCallback(((correlationData, b, s) -> {
                if (!b) {
                    System.out.println("fail" + s);
                }
        }));
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(final ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(producerJackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri("amqps://gkwqbyhy:K-prsg6FY2Go90V7_TMPe-MVi44heKzO@sparrow.rmq.cloudamqp.com/gkwqbyhy");
        connectionFactory.setUsername("gkwqbyhy");
        connectionFactory.setPassword("K-prsg6FY2Go90V7_TMPe-MVi44heKzO");
        connectionFactory.setPort(5671);
        connectionFactory.setVirtualHost("gkwqbyhy");
        return connectionFactory;
    }
}
