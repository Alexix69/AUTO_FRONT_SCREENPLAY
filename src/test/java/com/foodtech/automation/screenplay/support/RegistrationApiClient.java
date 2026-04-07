package com.foodtech.automation.screenplay.support;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RegistrationApiClient {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static void register(RegistrationData data) {
        String body = String.format(
                "{\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                data.email(), data.username(), data.password()
        );

        sendRegistration(body);
    }

    public static void registerWithRole(RegistrationData data, String role) {
        String body = String.format(
                "{\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                data.email(), data.username(), data.password(), role
        );

        sendRegistration(body);
    }

    private static void sendRegistration(String body) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/auth/register"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status != 200 && status != 201) {
                throw new IllegalStateException(
                        "Setup failed: backend registration unavailable (status " + status + ")"
                );
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Setup failed: backend registration unavailable (" + e.getMessage() + ")", e);
        }
    }
}
