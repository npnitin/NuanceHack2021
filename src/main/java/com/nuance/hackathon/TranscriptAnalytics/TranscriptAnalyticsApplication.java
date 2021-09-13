package com.nuance.hackathon.TranscriptAnalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nuance.*")
public class TranscriptAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscriptAnalyticsApplication.class, args);
	}

}
