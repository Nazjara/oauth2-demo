package com.nazjara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ResourceServer2Application {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServer2Application.class, args);
	}
}
