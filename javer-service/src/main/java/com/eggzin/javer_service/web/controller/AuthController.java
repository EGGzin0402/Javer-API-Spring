package com.eggzin.javer_service.web.controller;

import com.eggzin.javer_service.web.dto.ClienteResponseDto;
import com.eggzin.javer_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.javer_service.proxy.AuthProxy;
import com.eggzin.javer_service.web.dto.UsuarioLoginDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth Controller", description = "Expõe os endpoints dos recursos de autenticação vindos do serviço 'Cliente-Service'")
@RestController
@RequestMapping("javer-service/auth")
public class AuthController {
	
	@Autowired
	private AuthProxy proxy;

	@Operation(
			summary = "Login",
			description = "Faz login com os dados do usuário.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Cliente encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Dados inseridos não conferem com nenhum usuário",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@PostMapping
	public ResponseEntity<?> login(@RequestBody UsuarioLoginDto dto){
		
		return proxy.auth(dto);
		
		/*
		 * return new RestTemplate() .postForEntity("http://localhost:8000/auth", dto,
		 * JwtToken.class);
		 */
	}

}
