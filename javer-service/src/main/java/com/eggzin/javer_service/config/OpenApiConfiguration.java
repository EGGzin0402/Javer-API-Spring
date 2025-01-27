package com.eggzin.javer_service.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
	
	@Bean
	public OpenAPI customOpenAI() {
		
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("security", securityScheme()))
				.info(
						new io.swagger.v3.oas.models.info.Info()
						.title("Javer-Service")
						.version("v1")
						.description("Documentação do serviço/API principal do Banco Javer")
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
