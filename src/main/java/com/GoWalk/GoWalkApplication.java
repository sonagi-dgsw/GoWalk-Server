package com.GoWalk;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableJpaAuditing
public class GoWalkApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        require(dotenv, "DB_URL");
        require(dotenv, "DB_USERNAME");
        require(dotenv, "DB_PASSWORD");

        SpringApplication.run(GoWalkApplication.class, args);
    }

    private static void require(Dotenv dotenv, String key) {
        String v = dotenv.get(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        System.setProperty(key, v);
    }
}
