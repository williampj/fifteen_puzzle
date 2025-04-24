package com.william.puzzle.constants;

public class LogMessages {
    public static final String INVALID_JWT = "Invalid JWT Token";
    public static final String INVALID_JWT_WITH_REASON = "Invalid JWT Token: {}";

    public static final String NOTIFICATION_SENT_TO_QUEUE = "Admin notification of type: {} for userId: {} sent to routingKey: {}";
    public static final String LISTENER_RECEIVED_NOTIFICATION = "Received notification of type: {} for user {}";
    public static final String SSE_EMITTER_ERROR = "SSE error for userId {}: {}";
    public static final String SSE_CONNECTION_NOT_FOUND = "SSE connection not found for userId {}";
    public static final String SSE_NOTIFICATION_FAILURE = "Failed to send SSE notification to userId {}";
}
