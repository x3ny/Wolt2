package org.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

import javax.swing.*;

@SpringBootApplication
@EntityScan(basePackages = "org.example.Classes")
public class WoltWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WoltWebApplication.class, args);
    }
}
