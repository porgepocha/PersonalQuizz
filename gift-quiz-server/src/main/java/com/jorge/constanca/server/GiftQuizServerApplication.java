package com.jorge.constanca.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class GiftQuizServerApplication {

    public static void main(String[] args) {
        configureDatasourceFromEnvironment();
        SpringApplication.run(GiftQuizServerApplication.class, args);
    }

    private static void configureDatasourceFromEnvironment() {
        String rawUrl = firstNonBlank(
                System.getenv("SPRING_DATASOURCE_URL"),
                System.getenv("DATABASE_URL")
        );
        if (rawUrl == null) {
            return;
        }

        if (rawUrl.startsWith("jdbc:")) {
            System.setProperty("spring.datasource.url", rawUrl);
            return;
        }

        if (!rawUrl.startsWith("postgres://") && !rawUrl.startsWith("postgresql://")) {
            return;
        }

        try {
            URI uri = new URI(rawUrl);
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost();
            if (uri.getPort() != -1) {
                jdbcUrl += ":" + uri.getPort();
            }
            jdbcUrl += uri.getPath();
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                jdbcUrl += "?" + uri.getQuery();
            }
            System.setProperty("spring.datasource.url", jdbcUrl);

            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                String[] parts = userInfo.split(":", 2);
                if (System.getenv("SPRING_DATASOURCE_USERNAME") == null
                        && System.getenv("DATABASE_USERNAME") == null
                        && parts.length > 0
                        && !parts[0].isBlank()) {
                    System.setProperty("spring.datasource.username", parts[0]);
                }
                if (System.getenv("SPRING_DATASOURCE_PASSWORD") == null
                        && System.getenv("DATABASE_PASSWORD") == null
                        && parts.length == 2) {
                    System.setProperty("spring.datasource.password", parts[1]);
                }
            }
        } catch (URISyntaxException ignored) {
            // Leave Spring Boot to report the original datasource issue if the URL is malformed.
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
