package com.compdes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class CompdesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompdesApplication.class, args);
	}

}
