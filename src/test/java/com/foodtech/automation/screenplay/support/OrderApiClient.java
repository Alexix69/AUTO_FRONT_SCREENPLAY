package com.foodtech.automation.screenplay.support;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class OrderApiClient {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static String createOrder(String meseroToken, int tableNumber, List<OrderItem> items) {
        StringBuilder products = new StringBuilder("[");
        boolean first = true;
        for (OrderItem item : items) {
            String type = stationToProductType(item.station());
            for (int i = 0; i < item.quantity(); i++) {
                if (!first) products.append(",");
                products.append(String.format(
                        "{\"name\":\"%s\",\"type\":\"%s\"}",
                        item.productName(), type));
                first = false;
            }
        }
        products.append("]");

        String body = String.format(
                "{\"tableNumber\":\"T-%d\",\"products\":%s}",
                tableNumber, products);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/orders"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + meseroToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status < 200 || status > 299) {
                throw new IllegalStateException(
                        "Setup failed: order creation returned " + status + " — " + response.body());
            }
            return extractOrderId(response.body());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Setup failed: order creation unavailable (" + e.getMessage() + ")", e);
        }
    }

    public static OrderItem createOrderItem(String productName, int quantity, String station) {
        return new OrderItem(productName, quantity, station);
    }

    private static String stationToProductType(String station) {
        return switch (station) {
            case "BAR" -> "DRINK";
            case "HOT_KITCHEN" -> "HOT_DISH";
            case "COLD_KITCHEN" -> "COLD_DISH";
            default -> throw new IllegalArgumentException("Unknown station: " + station);
        };
    }

    private static String extractOrderId(String responseBody) {
        int idx = responseBody.indexOf("\"id\":");
        if (idx == -1) return null;
        int start = idx + 5;
        while (start < responseBody.length() && responseBody.charAt(start) == ' ') start++;
        int end = start;
        while (end < responseBody.length()
                && responseBody.charAt(end) != ','
                && responseBody.charAt(end) != '}') end++;
        return responseBody.substring(start, end).trim();
    }
}
