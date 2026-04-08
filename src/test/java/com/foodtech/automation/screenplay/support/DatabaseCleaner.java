package com.foodtech.automation.screenplay.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class DatabaseCleaner {

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "foodtech_db";
    private static final String DB_USER = "foodtech_user";
    private static final String DB_PASS = "foodtech_pass";

    public static void completeAllActiveKitchenTasks() {
        String sql = "UPDATE tasks SET status='COMPLETED', completed_at=NOW() " +
                "WHERE status IN ('PENDING','IN_PREPARATION') " +
                "AND station IN ('HOT_KITCHEN','COLD_KITCHEN')";
        runSql(sql);
    }

    private static void runSql(String sql) {
        ProcessBuilder pb = new ProcessBuilder(
                "psql",
                "-h", DB_HOST,
                "-p", DB_PORT,
                "-U", DB_USER,
                "-d", DB_NAME,
                "-c", sql
        );
        pb.environment().put("PGPASSWORD", DB_PASS);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(line -> {});
            }
            process.waitFor();
        } catch (Exception ignored) {
        }
    }
}
