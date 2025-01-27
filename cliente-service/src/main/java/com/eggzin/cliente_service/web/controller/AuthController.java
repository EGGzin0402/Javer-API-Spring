package com.eggzin.cliente_service.web.controller;

import com.eggzin.cliente_service.web.dto.ClienteResponseDto;
import com.eggzin.cliente_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.cliente_service.jwt.JwtToken;
import com.eggzin.cliente_service.jwt.JwtUserDetailsService;
import com.eggzin.cliente_service.web.dto.UsuarioLoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Auth Controller", description = "Contém os endpoints para o recurso de autenticação")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/auth")
public class AuthController {
	
	private final JwtUserDetailsService detailsService;
	private final AuthenticationManager authenticationManager;

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
	public ResponseEntity<?> auth(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request){
		
			log.info(dto.getPassword());
			log.info(dto.getUsername());
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			authenticationManager.authenticate(authenticationToken);
			
			JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
			
			return ResponseEntity.ok(token);
			
	}

}
