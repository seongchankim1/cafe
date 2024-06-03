package com.sparta.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeApplication.class, args);
	}

}
