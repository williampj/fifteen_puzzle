package com.william.puzzle.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AdminNotificationType {
    GAME_DELETED("The game '%s' was deleted by an admin user"),
    USER_DELETED("The account '%s' was deleted by an admin user");

    private final String messageTemplate;

    public String format(String name) {
        return String.format(messageTemplate, name);
    }
}
