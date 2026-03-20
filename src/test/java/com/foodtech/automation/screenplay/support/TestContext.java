package com.foodtech.automation.screenplay.support;

public class TestContext {

    private static final ThreadLocal<RegistrationData> USER = new ThreadLocal<>();
    private static final ThreadLocal<RegistrationData> CONFLICTING_USER = new ThreadLocal<>();

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

    public static void clear() {
        USER.remove();
        CONFLICTING_USER.remove();
    }
}
