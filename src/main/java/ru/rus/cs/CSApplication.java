package ru.rus.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CSApplication {

	public static void main(String[] args) {
		SpringApplication.run(CSApplication.class, args);
	}

}
