package com.eggzin.cliente_service.web.controller;

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

@Tag(name = "Ciente Controller", description = "Contém os endoints para as operações com ciente no Banco de Dados")
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/clientes")
public class ClienteController {
	
	private final ClienteService clienteService;
	private final UsuarioService usuarioService;
	
	@Autowired
	private Environment environment;
	
	@Operation(summary = "TESTE")
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
