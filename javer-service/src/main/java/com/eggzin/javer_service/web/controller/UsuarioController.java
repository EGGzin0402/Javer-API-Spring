package com.eggzin.javer_service.web.controller;

import com.eggzin.javer_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.javer_service.proxy.UsuarioProxy;
import com.eggzin.javer_service.web.dto.PasswordEditDto;
import com.eggzin.javer_service.web.dto.UsuarioCreateDto;
import com.eggzin.javer_service.web.dto.UsuarioResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuarios Controller", description = "Expõe os endpoints do recurso 'usuarios' vindos do serviço 'Cliente-Service'")
@RestController
@RequestMapping("javer-service/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioProxy proxy;

	@Operation(
			summary = "Criação de usuário",
			description = "Endpoint para criar um novo usuário com base nos dados fornecidos.",
			responses = {
					@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
					@ApiResponse(responseCode = "409", description = "Usuário já cadastrado",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@PostMapping
	public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto dto){
		
		return proxy.create(dto);
		
	}

	@Operation(
			summary = "Consultar usuário por ID",
			security = @SecurityRequirement(name = "security"),
			description = "Busca os detalhes de um usuário específico pelo ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Usuário recuperado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "403", description = "Sem permissão para buscar os dados do usuário",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> getById(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id){
		return proxy.getById(token, id);
	}

	@Operation(
			summary = "Alterar senha do usuário",
			security = @SecurityRequirement(name = "security"),
			description = "Atualiza a senha de um usuário existente.",
			responses = {
					@ApiResponse(responseCode = "204", description = "Senha do usuário alterada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "403", description = "Sem permissão para alterar os dados do usuário",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@PatchMapping("/{id}")
	public ResponseEntity<Void> editPassword(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody PasswordEditDto dto){
		return proxy.editPassword(token, id, dto);
	}
	
	
}
