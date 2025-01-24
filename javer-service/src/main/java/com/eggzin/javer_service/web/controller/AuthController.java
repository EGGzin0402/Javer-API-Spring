package com.eggzin.javer_service.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.javer_service.proxy.AuthProxy;
import com.eggzin.javer_service.web.dto.UsuarioLoginDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth Controller")
@RestController
@RequestMapping("javer-service/auth")
public class AuthController {
	
	@Autowired
	private AuthProxy proxy;
	
	@PostMapping
	public ResponseEntity<?> login(@RequestBody UsuarioLoginDto dto){
		
		return proxy.auth(dto);
		
		/*
		 * return new RestTemplate() .postForEntity("http://localhost:8000/auth", dto,
		 * JwtToken.class);
		 */
	}

}
