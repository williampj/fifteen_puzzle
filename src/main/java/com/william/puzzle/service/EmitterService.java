package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

/**
 * Interface for managing Server-Sent Event (SSE) emitters and sending
 * administrative notifications to connected clients.
 */
public interface EmitterService {

    /**
     * Initializes and registers a new SSE emitter for the given user.
     *
     * @param userId the ID of the user to associate with the emitter
     * @return a new {@link SseEmitter} instance configured with cleanup logic
     */
    SseEmitter setupEmitter(UUID userId);

    /**
     * Sends an admin notification to the specified user's SSE connection.
     * If no active connection is found, the notification is silently ignored.
     *
     * @param userId       the ID of the user to send the notification to
     * @param notification the admin notification payload
     */
    void sendAdminNotification(UUID userId, AdminNotification notification);
}
