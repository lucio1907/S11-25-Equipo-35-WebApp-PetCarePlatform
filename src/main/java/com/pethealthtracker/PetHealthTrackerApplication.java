package com.pethealthtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PetHealthTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetHealthTrackerApplication.class, args);
    }
}
