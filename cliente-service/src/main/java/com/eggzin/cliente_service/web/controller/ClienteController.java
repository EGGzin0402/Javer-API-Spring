package com.eggzin.cliente_service.web.controller;

import com.eggzin.cliente_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.cliente_service.entity.Cliente;
import com.eggzin.cliente_service.jwt.JwtUserDetails;
import com.eggzin.cliente_service.service.ClienteService;
import com.eggzin.cliente_service.service.UsuarioService;
import com.eggzin.cliente_service.web.dto.ClienteCreateDto;
import com.eggzin.cliente_service.web.dto.ClienteEditDto;
import com.eggzin.cliente_service.web.dto.ClienteResponseDto;
import com.eggzin.cliente_service.web.dto.mapper.ClienteMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Ciente Controller", description = "Contém os endpoints para as operações com clientes no Banco de Dados")
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/clientes")
public class ClienteController {
	
	private final ClienteService clienteService;
	private final UsuarioService usuarioService;
	
	@Autowired
	private Environment environment;

	@Operation(
			summary = "Criação de cliente",
			security = @SecurityRequirement(name = "security"),
			description = "Endpoint para criar um novo cliente com base nos dados fornecidos.",
			responses = {
					@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "409", description = "Cliente já cadastrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@PostMapping
	public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
													@AuthenticationPrincipal JwtUserDetails userDetails){
		
		var port = environment.getProperty("local.server.port");
		
		Cliente cliente = ClienteMapper.toCliente(dto);
		cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
		clienteService.salvar(cliente);
		
		ClienteResponseDto resDto = ClienteMapper.toDto(cliente);
		resDto.setPort(port);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
	}

	@Operation(
			summary = "Consultar cliente por ID",
			security = @SecurityRequirement(name = "security"),
			description = "Busca os detalhes de um cliente específico pelo ID.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Cliente encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "403", description = "Sem permissão para buscar os dados do cliente",
							content = @Content(mediaType = "application/json")),
			}
	)
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id,
													@AuthenticationPrincipal JwtUserDetails userDetails){
		var port = environment.getProperty("local.server.port");
		
		Cliente cliente = clienteService.buscarPorId(id);
		
		if (!cliente.getUsuario().getId().equals(userDetails.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
		ClienteResponseDto resDto = ClienteMapper.toDto(cliente);
		resDto.setPort(port);
		return ResponseEntity.ok(resDto);
	}

	@Operation(
			summary = "Atualizar cliente",
			security = @SecurityRequirement(name = "security"),
			description = "Atualiza os dados de um cliente existente.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "403", description = "Sem permissão para alterar os dados do cliente",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}
	)
	@PatchMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> edit(@PathVariable Long id, @RequestBody @Valid ClienteEditDto dto,
												@AuthenticationPrincipal JwtUserDetails userDetails){
		
		var port = environment.getProperty("local.server.port");
		
		Cliente cliente = ClienteMapper.toCliente(dto);
		
		Cliente cliAuth = clienteService.buscarPorId(id);
		
		if (!cliAuth.getUsuario().getId().equals(userDetails.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
		
		clienteService.editar(id, cliente);
		
		ClienteResponseDto resDto = ClienteMapper.toDto(cliente);
		resDto.setPort(port);
		
		return ResponseEntity.ok(resDto);
	}

	@Operation(
			summary = "Excluir cliente",
			security = @SecurityRequirement(name = "security"),
			description = "Remove um cliente do sistema pelo ID.",
			responses = {
					@ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "403", description = "Sem permissão para excluir o cliente",
							content = @Content(mediaType = "application/json")),
			}
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails){
		
		Cliente cliAuth = clienteService.buscarPorId(id);
		
		if (!cliAuth.getUsuario().getId().equals(userDetails.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
		
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}

}
