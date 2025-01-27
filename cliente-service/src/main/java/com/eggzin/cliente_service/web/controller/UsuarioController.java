package com.eggzin.cliente_service.web.controller;

import com.eggzin.cliente_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.cliente_service.entity.Usuario;
import com.eggzin.cliente_service.service.UsuarioService;
import com.eggzin.cliente_service.web.dto.PasswordEditDto;
import com.eggzin.cliente_service.web.dto.UsuarioCreateDto;
import com.eggzin.cliente_service.web.dto.UsuarioResponseDto;
import com.eggzin.cliente_service.web.dto.mapper.UsuarioMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Usuarios Controller", description = "Contém os endpoints para as operações com usuários no Banco de Dados")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;

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
	public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto dto){
		Usuario usuario = usuarioService.salvar(UsuarioMapper.toUsuario(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
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
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
		Usuario usuario = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(UsuarioMapper.toDto(usuario));
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
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<Void> editPassword(@PathVariable Long id, @Valid @RequestBody PasswordEditDto dto){
		usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.noContent().build();
	}

}
