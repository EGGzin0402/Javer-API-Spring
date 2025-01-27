package com.eggzin.cliente_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration {
	
	@Bean
	public OpenAPI customOpenAI() {
		
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("security", securityScheme()))
				.info(
						new io.swagger.v3.oas.models.info.Info()
						.title("Cliente-Service")
						.version("v1")
						.license(new License()
								.name("Apache 2.0")
								.url("http://springdoc.org"))
						);
	}
	
	private SecurityScheme securityScheme() {
		return new SecurityScheme()
				.description("Insira um bearer token")
				.type(SecurityScheme.Type.HTTP)
				.in(SecurityScheme.In.HEADER)
				.scheme("bearer")
				.bearerFormat("JWT")
				.name("security");
	}

}
