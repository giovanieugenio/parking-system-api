package com.park.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocOpenApiConfig {

	@Bean
	  OpenAPI openAPI(){
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("security", securityScheme()))
				.info(new Info().title("REST API - Spring Park")
				.description("API for vehicle parking management")
				.version("v1")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
				.contact(new Contact().name("Giovani Eugenio").email("park-spring@hotmail.com")));
	}

	private SecurityScheme securityScheme(){
		return new SecurityScheme()
				.description("Enter a valid bearer token to proceed: ")
				.type(SecurityScheme.Type.HTTP)
				.in(SecurityScheme.In.HEADER)
				.scheme("bearer")
				.bearerFormat("JWT")
				.name("security");
	}
}
