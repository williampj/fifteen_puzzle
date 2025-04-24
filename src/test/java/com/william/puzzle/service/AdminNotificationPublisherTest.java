package com.william.puzzle.service;

import com.william.puzzle.constants.MessageBrokerConstants;
import com.william.puzzle.dto.AdminNotification;
import com.william.puzzle.enums.AdminNotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static com.william.puzzle.constants.MessageBrokerConstants.GAME_DELETED_ROUTING_KEY;
import static com.william.puzzle.constants.MessageBrokerConstants.USER_DELETED_ROUTING_KEY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AdminNotificationPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private AdminNotificationPublisherImpl publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new AdminNotificationPublisherImpl(rabbitTemplate);
    }

    @Test
    void sendUserDeletedNotification_SendsToCorrectExchangeAndRoutingKey() {
        UUID userId = UUID.randomUUID();
        AdminNotification notification = new AdminNotification(userId, AdminNotificationType.USER_DELETED, "User was removed");

        publisher.sendUserDeletedNotification(notification);

        verify(rabbitTemplate).convertAndSend(
                MessageBrokerConstants.ADMIN_NOTIFICATION_EXCHANGE,
                USER_DELETED_ROUTING_KEY,
                notification
        );
    }

    @Test
    void sendGameDeletedNotification_SendsToCorrectExchangeAndRoutingKey() {
        UUID userId = UUID.randomUUID();
        AdminNotification notification = new AdminNotification(userId, AdminNotificationType.GAME_DELETED, "Game was removed");

        publisher.sendGameDeletedNotification(notification);

        verify(rabbitTemplate).convertAndSend(
                MessageBrokerConstants.ADMIN_NOTIFICATION_EXCHANGE,
                GAME_DELETED_ROUTING_KEY,
                notification
        );
    }
}
