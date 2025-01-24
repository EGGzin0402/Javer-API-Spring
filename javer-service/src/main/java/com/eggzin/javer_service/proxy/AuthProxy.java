package com.eggzin.javer_service.proxy;

import com.eggzin.javer_service.web.exception.FeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eggzin.javer_service.web.dto.UsuarioLoginDto;

import jakarta.validation.Valid;

@FeignClient(name = "auth", url = "localhost:8000/cliente-service/auth", configuration = FeignErrorDecoder.class)
public interface AuthProxy {
	
	@PostMapping
	public ResponseEntity<?> auth(@RequestBody @Valid UsuarioLoginDto dto);

}
