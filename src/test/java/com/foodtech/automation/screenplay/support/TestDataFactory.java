package com.foodtech.automation.screenplay.support;

import java.time.Instant;
import java.util.UUID;

public class TestDataFactory {

    public static String generateEmail() {
        return "test+" + Instant.now().toEpochMilli() + "@restaurant.com";
    }

    public static String generateUsername() {
        return "user" + Instant.now().toEpochMilli();
    }

    public static String generatePassword(String salt) {
        return "Pass" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + salt;
    }

    public static RegistrationData createRegistrationData() {
        return new RegistrationData(generateEmail(), generateUsername(), generatePassword("A"), "MESERO");
    }
}
