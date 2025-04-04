package com.pitang.desafiopitangapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.pitang.desafiopitangapi")
public class DesafioPitangApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesafioPitangApiApplication.class, args);
    }

}
