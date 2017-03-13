package com.thermofisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.thermofisher"})
public class SpringmailsenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringmailsenderApplication.class, args);
	}
}
