package com.eggzin.javer_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eggzin.javer_service.web.dto.ClienteCreateDto;
import com.eggzin.javer_service.web.dto.ClienteEditDto;
import com.eggzin.javer_service.web.dto.ClienteResponseDto;
import com.eggzin.javer_service.web.exception.FeignErrorDecoder;

import jakarta.validation.Valid;

@FeignClient(name = "clientes", url = "localhost:8000/cliente-service/clientes", configuration = FeignErrorDecoder.class)
public interface ClienteProxy {
	
	@PostMapping
	public ResponseEntity<ClienteResponseDto> create(@RequestHeader("Authorization") String token, @RequestBody @Valid ClienteCreateDto dto);
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> getById(@RequestHeader("Authorization") String token, @PathVariable Long id);
	
	@PatchMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> edit(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody @Valid ClienteEditDto dto);
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, @PathVariable Long id);

}
