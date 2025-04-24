package com.william.puzzle.controller;

import com.william.puzzle.constants.ApiPaths;
import com.william.puzzle.service.EmitterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.NOTIFICATIONS)
public class NotificationController {
    private final EmitterServiceImpl emitterService;

    @GetMapping(ApiPaths.SUBSCRIBE)
    public SseEmitter subscribe(@PathVariable UUID userId) {
        return emitterService.setupEmitter(userId);
    }
}
