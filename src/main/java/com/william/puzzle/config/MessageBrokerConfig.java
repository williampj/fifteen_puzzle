package com.william.puzzle.config;

import com.william.puzzle.constants.MessageBrokerConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBrokerConfig {

    @Bean
    public Queue adminNotificationQueue() {
        return new Queue(MessageBrokerConstants.ADMIN_NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange adminNotificationExchange() {
        return new TopicExchange(MessageBrokerConstants.ADMIN_NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding adminNotificationBinding() {
        return BindingBuilder
                .bind(adminNotificationQueue())
                .to(adminNotificationExchange())
                .with(MessageBrokerConstants.ADMIN_BINDING_WILDCARD);
    }
}
