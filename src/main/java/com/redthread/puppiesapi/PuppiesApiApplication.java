package com.redthread.puppiesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PuppiesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuppiesApiApplication.class, args);
    }

}
