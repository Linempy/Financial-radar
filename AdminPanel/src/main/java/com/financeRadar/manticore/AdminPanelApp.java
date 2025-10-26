package com.financeRadar.manticore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.financeRadar.manticore")
public class AdminPanelApp {
    public static void main(String[] args) {
        SpringApplication.run(AdminPanelApp.class, args);
    }
}