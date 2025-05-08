package me.leeyeeun.bookhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookHubApplication.class, args);
    }

}
