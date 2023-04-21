package com.devex.iplstats;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IplStatsWithMongoApplication {
	
	@PostConstruct
	public void setTimeZone() {
	   TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	public static void main(String[] args) {
		SpringApplication.run(IplStatsWithMongoApplication.class, args);
	}

}
