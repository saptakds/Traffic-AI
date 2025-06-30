package com.saptak.trafficai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TrafficAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrafficAiApplication.class, args);
	}

}
