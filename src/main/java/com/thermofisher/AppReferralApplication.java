package com.thermofisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.thermofisher"})
public class AppReferralApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AppReferralApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppReferralApplication.class);
	}
}
