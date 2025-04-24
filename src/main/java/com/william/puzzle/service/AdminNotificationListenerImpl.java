package com.william.puzzle.service;

import com.william.puzzle.constants.MessageBrokerConstants;
import com.william.puzzle.dto.AdminNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.william.puzzle.constants.LogMessages.LISTENER_RECEIVED_NOTIFICATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminNotificationListenerImpl implements AdminNotificationListener {
    private final EmitterServiceImpl emitterService;

    @RabbitListener(queues = MessageBrokerConstants.ADMIN_NOTIFICATION_QUEUE)
    public void receiveNotification(AdminNotification notification) {
        logNotificationReceived(notification);
        emitterService.sendAdminNotification(notification.userId(), notification);
    }

    private void logNotificationReceived(AdminNotification notification) {
        log.info(LISTENER_RECEIVED_NOTIFICATION,
                notification.type().name(),
                notification.userId());
    }
}
