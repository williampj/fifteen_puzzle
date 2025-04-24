package com.william.puzzle.service;

import com.william.puzzle.constants.MessageBrokerConstants;
import com.william.puzzle.dto.AdminNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.william.puzzle.constants.LogMessages.NOTIFICATION_SENT_TO_QUEUE;
import static com.william.puzzle.constants.MessageBrokerConstants.GAME_DELETED_ROUTING_KEY;
import static com.william.puzzle.constants.MessageBrokerConstants.USER_DELETED_ROUTING_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminNotificationPublisherImpl implements AdminNotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void sendUserDeletedNotification(AdminNotification notification) {
        rabbitTemplate.convertAndSend(
                MessageBrokerConstants.ADMIN_NOTIFICATION_EXCHANGE,
                USER_DELETED_ROUTING_KEY,
                notification);

        logNotificationSent(notification, USER_DELETED_ROUTING_KEY);
    }

    public void sendGameDeletedNotification(AdminNotification notification) {
        rabbitTemplate.convertAndSend(
                MessageBrokerConstants.ADMIN_NOTIFICATION_EXCHANGE,
                GAME_DELETED_ROUTING_KEY,
                notification);

        logNotificationSent(notification, GAME_DELETED_ROUTING_KEY);
    }

    private void logNotificationSent(AdminNotification notification, String routingKey) {
        log.info(NOTIFICATION_SENT_TO_QUEUE, notification.type().name(), notification.userId(), routingKey);
    }
}
