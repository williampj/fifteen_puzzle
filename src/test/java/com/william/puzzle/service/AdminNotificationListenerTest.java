package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;
import com.william.puzzle.enums.AdminNotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class AdminNotificationListenerTest {

    private EmitterServiceImpl emitterService;
    private AdminNotificationListenerImpl listener;

    @BeforeEach
    void setUp() {
        emitterService = mock(EmitterServiceImpl.class);
        listener = new AdminNotificationListenerImpl(emitterService);
    }

    @Test
    void receiveNotification_ForwardsNotificationToEmitterService() {
        UUID userId = UUID.randomUUID();
        AdminNotification notification = new AdminNotification(
                userId,
                AdminNotificationType.USER_DELETED,
                "User was deleted for violation."
        );

        listener.receiveNotification(notification);

        verify(emitterService).sendAdminNotification(userId, notification);
    }
}
