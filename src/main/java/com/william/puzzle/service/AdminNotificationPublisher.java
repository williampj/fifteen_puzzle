package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;

/**
 * Interface for publishing administrative notifications to a message broker.
 */
public interface AdminNotificationPublisher {

    /**
     * Sends a notification indicating a user was deleted.
     *
     * @param notification the notification containing user and event details
     */
    void sendUserDeletedNotification(AdminNotification notification);

    /**
     * Sends a notification indicating a game was deleted.
     *
     * @param notification the notification containing game and event details
     */
    void sendGameDeletedNotification(AdminNotification notification);
}
