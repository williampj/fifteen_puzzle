package com.william.puzzle.service;

import com.william.puzzle.dto.AdminNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.william.puzzle.constants.LogMessages.SSE_CONNECTION_NOT_FOUND;
import static com.william.puzzle.constants.LogMessages.SSE_EMITTER_ERROR;
import static com.william.puzzle.constants.LogMessages.SSE_NOTIFICATION_FAILURE;
import static com.william.puzzle.constants.MessageBrokerConstants.SSE_ADMIN_NOTIFICATION_EVENT;

@Slf4j
@Service
public class EmitterServiceImpl implements EmitterService {
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter setupEmitter(UUID userId) {
        var sseEmitter = new SseEmitter(0L);
        emitters.put(userId, sseEmitter);

        Runnable remove = () -> emitters.remove(userId);
        sseEmitter.onCompletion(remove);
        sseEmitter.onTimeout(remove);
        sseEmitter.onError(e -> {
            remove.run();
            log.error(SSE_EMITTER_ERROR, userId, e.getMessage());
        });

        return sseEmitter;
    }

    public void sendAdminNotification(UUID userId, AdminNotification notification) {
        var sseEmitter = emitters.get(userId);
        if (sseEmitter == null) {
            log.info(SSE_CONNECTION_NOT_FOUND, userId);
            return;
        }

        try {
            sseEmitter.send(SseEmitter.event()
                            .name(SSE_ADMIN_NOTIFICATION_EVENT)
                            .data(notification));
        } catch (Exception e) {
            emitters.remove(userId);
            log.error(SSE_NOTIFICATION_FAILURE, userId);
        }
    }
}
