package com.william.puzzle.constants;

public class MessageBrokerConstants {
    public static final String ADMIN_NOTIFICATION_QUEUE = "admin.notifications.queue";
    public static final String ADMIN_NOTIFICATION_EXCHANGE = "admin.notifications.exchange";
    public static final String ADMIN_BINDING_WILDCARD = "admin.#";
    public static final String SSE_ADMIN_NOTIFICATION_EVENT = "admin-notification";

    public static final String USER_DELETED_ROUTING_KEY = "admin.user.deleted";
    public static final String GAME_DELETED_ROUTING_KEY = "admin.game.deleted";
}
