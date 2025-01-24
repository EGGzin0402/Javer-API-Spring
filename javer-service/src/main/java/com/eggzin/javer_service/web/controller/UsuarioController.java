package com.eggzin.javer_service.web.controller;

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

@Tag(name = "Usuarios Controller")
@RestController
@RequestMapping("javer-service/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioProxy proxy;
	
	@PostMapping
	public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto dto){
		
		return proxy.create(dto);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> getById(@RequestHeader("Authorization") String token, @PathVariable Long id){
		return proxy.getById(token, id);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> editPassword(@RequestHeader("Authorization") String token, @PathVariable Long id, @Valid @RequestBody PasswordEditDto dto){
		return proxy.editPassword(token, id, dto);
	}
	
	
}
