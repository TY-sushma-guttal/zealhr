package com.te.zealhr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZealhrBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZealhrBackendApplication.class, args);
	}

}
