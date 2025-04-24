package com.william.puzzle.constants;

public class ApiPaths {
    public static final String API_PREFIX = "/api/v1";

    // ========= Base Paths =========
    public static final String ADMIN = API_PREFIX + "/admin";
    public static final String AUTH = API_PREFIX + "/auth";
    public static final String GAMES = API_PREFIX + "/games";
    public static final String USERS = API_PREFIX + "/users";
    public static final String NOTIFICATIONS = API_PREFIX + "/notifications";

    // ========= External Endpoint (for Admin) =========
    public static final String ACTUATOR = "/actuator";

    // ========= Users Endpoints (for Admin) =========
    public static final String SIGNUP = USERS;

    // ========= Auth Endpoints =========
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH = "/refresh";

    // ========= Admin Endpoints =========
    public static final String ALL_USERS = "/users";
    public static final String INDIVIDUAL_USER = "/users/{userId}";
    public static final String INACTIVE_USERS = "/users/inactive";
    public static final String ALL_GAMES = "/games";
    public static final String GAMES_BY_USER = "/users/{userId}/games";
    public static final String INDIVIDUAL_GAME = "/users/{userId}/games/{gameId}";
    public static final String INACTIVE_GAMES = "/games/inactive";
    public static final String COMPLETED_GAMES = "/games/completed";

    // ========= Games Endpoints =========
    public static final String GET_GAME = "/{gameId}";
    public static final String DELETE_GAME = "/{gameId}";
    public static final String MOVE = "/{gameId}/move";

    // ========= Notifications Endpoints =========
    public static final String SUBSCRIBE = "/subscribe/{userId}";

    // ========= Full URIs =========
    public static final String SIGNUP_FULL_URI = SIGNUP;
    public static final String LOGIN_FULL_URI = AUTH + LOGIN;
    public static final String REFRESH_FULL_URI = AUTH + REFRESH;
    public static final String ACTUATOR_FULL_URI = ACTUATOR;
}
