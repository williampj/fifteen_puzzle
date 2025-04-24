package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;

/**
 * Listener interface for receiving administrative notifications
 * from the message broker.
 */
public interface AdminNotificationListener {

    /**
     * Receives and processes an incoming admin notification from the queue.
     *
     * @param notification the admin notification payload received from the broker
     */
    void receiveNotification(AdminNotification notification);
}
