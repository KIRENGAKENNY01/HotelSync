package com.hotelmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@EnableJpaAuditing
public class HotelManagementApplication {

    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(HotelManagementApplication.class, args);
    }

    private static void loadDotEnv() {
        Path dotEnvPath = Path.of(".env");
        if (!Files.exists(dotEnvPath)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(dotEnvPath)) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                int separatorIndex = trimmedLine.indexOf('=');
                if (separatorIndex <= 0) {
                    continue;
                }

                String key = trimmedLine.substring(0, separatorIndex).trim();
                String value = trimmedLine.substring(separatorIndex + 1).trim();
                value = stripOptionalQuotes(value);

                if (!System.getenv().containsKey(key) && System.getProperty(key) == null) {
                    System.setProperty(key, value);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load .env file", ex);
        }
    }

    private static String stripOptionalQuotes(String value) {
        if (value.length() >= 2) {
            boolean doubleQuoted = value.startsWith("\"") && value.endsWith("\"");
            boolean singleQuoted = value.startsWith("'") && value.endsWith("'");
            if (doubleQuoted || singleQuoted) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
