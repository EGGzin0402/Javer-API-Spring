package com.eggzin.cliente_service.web.controller;

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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto dto){
		Usuario usuario = usuarioService.salvar(UsuarioMapper.toUsuario(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
		Usuario usuario = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(UsuarioMapper.toDto(usuario));
	}
	
	@PatchMapping("/{id}")
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<Void> editPassword(@PathVariable Long id, @Valid @RequestBody PasswordEditDto dto){
		usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
		return ResponseEntity.noContent().build();
	}

}
