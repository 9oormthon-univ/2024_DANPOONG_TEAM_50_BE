package com.example.mymoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class MymooApplication {
	public static void main(String[] args) {
		SpringApplication.run(MymooApplication.class, args);
	}
}
