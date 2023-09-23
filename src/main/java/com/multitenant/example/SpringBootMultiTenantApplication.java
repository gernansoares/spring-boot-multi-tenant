package com.multitenant.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringBootMultiTenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMultiTenantApplication.class, args);
	}

}
