package com.wileyedge.healthyrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.wileyedge.healthyrecipe")
public class HealthyRecipeWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthyRecipeWebServiceApplication.class, args);
	}

}
