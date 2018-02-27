package com.kurotkin.algoprof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RunAlgoProf {
    public static void main(String[] args) {
        SpringApplication.run(RunAlgoProf.class, args);
    }
}
