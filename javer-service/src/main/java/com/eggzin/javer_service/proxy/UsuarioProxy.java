package com.eggzin.javer_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eggzin.javer_service.web.dto.PasswordEditDto;
import com.eggzin.javer_service.web.dto.UsuarioCreateDto;
import com.eggzin.javer_service.web.dto.UsuarioResponseDto;
import com.eggzin.javer_service.web.exception.FeignErrorDecoder;

import jakarta.validation.Valid;


@FeignClient(name = "usuarios", url = "localhost:8000/cliente-service/usuarios", configuration = FeignErrorDecoder.class)
public interface UsuarioProxy {

	@PostMapping
	public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioCreateDto dto);
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> getById(@RequestHeader("Authorization") String token, @PathVariable Long id);
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> editPassword(@RequestHeader("Authorization") String token, @PathVariable Long id, @Valid @RequestBody PasswordEditDto dto);
	
}
