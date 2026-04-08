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

    public static Long getTaskIdForOrder(String operatorToken, String station, String orderId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/tasks/station/" + station))
                .timeout(Duration.ofSeconds(5))
                .header("Authorization", "Bearer " + operatorToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status < 200 || status > 299) {
                throw new IllegalStateException(
                        "Setup failed: GET tasks returned " + status + " — " + response.body());
            }
            return extractTaskIdForOrder(response.body(), orderId);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Setup failed: GET tasks unavailable (" + e.getMessage() + ")", e);
        }
    }

    public static int startTask(String operatorToken, Long taskId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/tasks/" + taskId + "/start"))
                .timeout(Duration.ofSeconds(5))
                .header("Authorization", "Bearer " + operatorToken)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Setup failed: PATCH task start unavailable (" + e.getMessage() + ")", e);
        }
    }

    public static int completeTask(String operatorToken, Long taskId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/tasks/" + taskId + "/complete"))
                .timeout(Duration.ofSeconds(5))
                .header("Authorization", "Bearer " + operatorToken)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Setup failed: PATCH task complete unavailable (" + e.getMessage() + ")", e);
        }
    }

    public static String getOrderStatus(String meseroToken, String orderId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/api/orders/" + orderId + "/status"))
                .timeout(Duration.ofSeconds(5))
                .header("Authorization", "Bearer " + meseroToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status < 200 || status > 299) {
                throw new IllegalStateException(
                        "Setup failed: GET order status returned " + status + " — " + response.body());
            }
            return extractStatusField(response.body());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Setup failed: GET order status unavailable (" + e.getMessage() + ")", e);
        }
    }

    private static String extractStatusField(String responseBody) {
        String key = "\"status\":\"";
        int idx = responseBody.indexOf(key);
        if (idx == -1) return null;
        int start = idx + key.length();
        int end = responseBody.indexOf("\"", start);
        if (end == -1) return null;
        return responseBody.substring(start, end);
    }

    private static String extractOrderId(String responseBody) {
        String key = "\"orderId\":";
        int idx = responseBody.indexOf(key);
        if (idx == -1) {
            key = "\"id\":";
            idx = responseBody.indexOf(key);
        }
        if (idx == -1) return null;
        int start = idx + key.length();
        while (start < responseBody.length() && responseBody.charAt(start) == ' ') start++;
        int end = start;
        while (end < responseBody.length()
                && responseBody.charAt(end) != ','
                && responseBody.charAt(end) != '}') end++;
        return responseBody.substring(start, end).trim();
    }

    private static Long extractTaskIdForOrder(String responseBody, String orderId) {
        String target = "\"orderId\":" + orderId;
        int idx = responseBody.indexOf(target);
        if (idx == -1) {
            target = "\"orderId\": " + orderId;
            idx = responseBody.indexOf(target);
        }
        if (idx == -1) return null;
        int objStart = responseBody.lastIndexOf("{", idx);
        int objEnd = responseBody.indexOf("}", idx);
        if (objStart == -1 || objEnd == -1) return null;
        String block = responseBody.substring(objStart, objEnd + 1);
        int idIdx = block.indexOf("\"id\":");
        if (idIdx == -1) return null;
        int start = idIdx + 5;
        while (start < block.length() && block.charAt(start) == ' ') start++;
        int end = start;
        while (end < block.length() && block.charAt(end) != ',' && block.charAt(end) != '}') end++;
        try {
            return Long.parseLong(block.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
