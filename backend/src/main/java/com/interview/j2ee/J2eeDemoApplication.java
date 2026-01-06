package com.interview.j2ee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class J2eeDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(J2eeDemoApplication.class, args);
    }
}
