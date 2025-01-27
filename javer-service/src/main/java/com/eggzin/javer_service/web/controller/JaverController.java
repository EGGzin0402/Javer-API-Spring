package com.eggzin.javer_service.web.controller;

import com.eggzin.javer_service.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.javer_service.entity.Cliente;
import com.eggzin.javer_service.proxy.ClienteProxy;
import com.eggzin.javer_service.util.ScoreUtils;
import com.eggzin.javer_service.web.dto.ClienteCreateDto;
import com.eggzin.javer_service.web.dto.ClienteEditDto;
import com.eggzin.javer_service.web.dto.ClienteResponseDto;
import com.eggzin.javer_service.web.dto.ScoreResponseDto;
import com.eggzin.javer_service.web.dto.mapper.ClienteMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Javer (General) Controller", description = "Expõe os endpoints do recurso 'clientes' vindos do serviço 'Cliente-Service'")
@RequiredArgsConstructor
@RestController
@RequestMapping("javer-service/clientes")
public class JaverController {
	
	@Autowired
	private ClienteProxy proxy;
	
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
	public ResponseEntity<ClienteResponseDto> create(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody ClienteCreateDto dto){
		
		return proxy.create(token, dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(dto, headers);
		 * 
		 * return new RestTemplate() .exchange("http://localhost:8000/clientes",
		 * HttpMethod.POST, entityReq, ClienteResponseDto.class);
		 */
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
	public ResponseEntity<ClienteResponseDto> getById(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id){
		
		return proxy.getById(token, id);
		
		/*
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<?> entityReq = new HttpEntity<>(headers);
		
		Long param = id;
		
		return new RestTemplate()
				.exchange("http://localhost:8000/clientes/{id}",HttpMethod.GET, entityReq, ClienteResponseDto.class, param);
		 */
	
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
	public ResponseEntity<ClienteResponseDto> edit(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id, @Valid @RequestBody ClienteEditDto dto){
		
		return proxy.edit(token, id, dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(dto, headers);
		 * 
		 * Long param = id;
		 * 
		 * RestTemplate restTemplate = new RestTemplate(new
		 * HttpComponentsClientHttpRequestFactory());
		 * 
		 * return restTemplate .exchange("http://localhost:8000/clientes/{id}",
		 * HttpMethod.PATCH, entityReq, ClienteResponseDto.class, param);
		 */
		
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
	public ResponseEntity<Void> delete(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id){
		return proxy.delete(token, id);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(headers);
		 * 
		 * Long param = id;
		 * 
		 * new RestTemplate()
		 * .exchange("http://localhost:8000/clientes/{id}",HttpMethod.DELETE, entityReq,
		 * Void.class, param);
		 * 
		 * return ResponseEntity.noContent().build();
		 */
	}

	@Operation(
			summary = "Calcular score do cliente",
			security = @SecurityRequirement(name = "security"),
			description = "Calcula o score de crédito de um cliente com base no saldo disponível.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Score calculado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScoreResponseDto.class))),
					@ApiResponse(responseCode = "401", description = "Usuário não autenticado",
							content = @Content(mediaType = "application/json")),
					@ApiResponse(responseCode = "403", description = "Sem permissão para ver os dados do cliente",
							content = @Content(mediaType = "application/json")),
			}
	)
	@GetMapping("/{id}/score")
	public ResponseEntity<ScoreResponseDto> calcularScore(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id){
		
		ClienteResponseDto cliResDto = proxy.getById(token, id).getBody();
		Cliente cliente = ClienteMapper.toCliente(cliResDto);
		ScoreResponseDto dto = new ScoreResponseDto();
		dto.setScoreCredito(ScoreUtils.calcScore(cliente.getSaldo()));
		
		return ResponseEntity.ok(dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(headers);
		 * 
		 * Long param = id;
		 * 
		 * ClienteResponseDto cliResDto = new RestTemplate()
		 * .exchange("http://localhost:8000/clientes/{id}", HttpMethod.GET, entityReq,
		 * ClienteResponseDto.class, param).getBody();
		 * 
		 * Cliente cliente = ClienteMapper.toCliente(cliResDto); ScoreResponseDto dto =
		 * new ScoreResponseDto();
		 * dto.setScoreCredito(ScoreUtils.calcScore(cliente.getSaldo()));
		 * 
		 * return ResponseEntity.ok(dto);
		 */
		
	}

}
