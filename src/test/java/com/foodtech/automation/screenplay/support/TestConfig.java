package com.foodtech.automation.screenplay.support;

public class TestConfig {

    public static String getBaseUrl() {
        String url = System.getenv("FOODTECH_BASE_URL");
        return (url != null && !url.isBlank()) ? url : "http://localhost:5173";
    }

    public static String getBackendBaseUrl() {
        String url = System.getenv("FOODTECH_BACKEND_URL");
        if (url == null || url.isBlank()) {
            url = System.getProperty("foodtech.backend.base.url");
        }
        return (url != null && !url.isBlank()) ? url : "http://localhost:8080";
    }
}
