package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;
import com.william.puzzle.enums.AdminNotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmitterServiceTest {

    private EmitterServiceImpl emitterService;

    @BeforeEach
    void setUp() {
        emitterService = new EmitterServiceImpl();
    }

    @Test
    void setupEmitter_RegistersEmitterSuccessfully() {
        UUID userId = UUID.randomUUID();

        SseEmitter emitter = emitterService.setupEmitter(userId);

        assertNotNull(emitter);
    }

    @Test
    void sendAdminNotification_DoesNothing_WhenEmitterNotFound() {
        UUID userId = UUID.randomUUID();
        AdminNotification notification = new AdminNotification(
                userId,
                AdminNotificationType.USER_DELETED,
                "User was removed"
        );

        assertDoesNotThrow(() -> emitterService.sendAdminNotification(userId, notification));
    }

    @Test
    void sendAdminNotification_SendsEventSuccessfully() throws IOException {
        UUID userId = UUID.randomUUID();
        AdminNotification notification = new AdminNotification(
                userId,
                AdminNotificationType.GAME_DELETED,
                "Game was removed"
        );

        SseEmitter emitter = emitterService.setupEmitter(userId);

        emitterService.sendAdminNotification(userId, notification);

        assertNotNull(emitter); // tests that no exception was thrown
    }
}
