package com.micro.SpringMicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.micro.SpringMicro.model")
public class SpringMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMicroApplication.class, args);
	}

}
