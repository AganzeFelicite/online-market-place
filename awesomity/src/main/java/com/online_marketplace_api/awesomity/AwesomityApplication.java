package com.online_marketplace_api.awesomity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Online Marketplace API", version = "v1", description = "API documentation for Online Marketplace"))

public class AwesomityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomityApplication.class, args);
	}

}
