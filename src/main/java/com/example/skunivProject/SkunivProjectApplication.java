package com.example.skunivProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SkunivProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkunivProjectApplication.class, args);
	}

}
