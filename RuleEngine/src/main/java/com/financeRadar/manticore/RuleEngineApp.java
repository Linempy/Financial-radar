package com.financeRadar.manticore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@SpringBootApplication
public class RuleEngineApp {
    public static void main(String[] args) {
        SpringApplication.run(RuleEngineApp.class, args);
    }
}