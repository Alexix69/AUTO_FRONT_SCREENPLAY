package com.foodtech.automation.screenplay.support;

public class TestContext {

    private static final ThreadLocal<RegistrationData> USER = new ThreadLocal<>();
    private static final ThreadLocal<RegistrationData> CONFLICTING_USER = new ThreadLocal<>();
    private static final ThreadLocal<LoginData> LOGIN_USER = new ThreadLocal<>();
    private static final ThreadLocal<String> EXPECTED_STATION = new ThreadLocal<>();
    private static final ThreadLocal<Long> TASK_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> OPERATOR_TOKEN = new ThreadLocal<>();

    public static void setUser(RegistrationData data) {
        USER.set(data);
    }

    public static RegistrationData getUser() {
        return USER.get();
    }

    public static void setConflictingUser(RegistrationData data) {
        CONFLICTING_USER.set(data);
    }

    public static RegistrationData getConflictingUser() {
        return CONFLICTING_USER.get();
    }

    public static void setLoginUser(LoginData data) {
        LOGIN_USER.set(data);
    }

    public static LoginData getLoginUser() {
        return LOGIN_USER.get();
    }

    public static void setExpectedStation(String station) {
        EXPECTED_STATION.set(station);
    }

    public static String getExpectedStation() {
        return EXPECTED_STATION.get();
    }

    public static void setTaskId(Long id) {
        TASK_ID.set(id);
    }

    public static Long getTaskId() {
        return TASK_ID.get();
    }

    public static void setOperatorToken(String token) {
        OPERATOR_TOKEN.set(token);
    }

    public static String getOperatorToken() {
        return OPERATOR_TOKEN.get();
    }

    public static void clear() {
        USER.remove();
        CONFLICTING_USER.remove();
        LOGIN_USER.remove();
        EXPECTED_STATION.remove();
        TASK_ID.remove();
        OPERATOR_TOKEN.remove();
    }
}

