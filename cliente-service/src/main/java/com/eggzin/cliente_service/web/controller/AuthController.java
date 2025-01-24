package com.eggzin.cliente_service.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.cliente_service.jwt.JwtToken;
import com.eggzin.cliente_service.jwt.JwtUserDetailsService;
import com.eggzin.cliente_service.web.dto.UsuarioLoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("cliente-service/auth")
public class AuthController {
	
	private final JwtUserDetailsService detailsService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping
	public ResponseEntity<?> auth(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request){
		
			log.info(dto.getPassword());
			log.info(dto.getUsername());
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			authenticationManager.authenticate(authenticationToken);
			
			JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
			
			return ResponseEntity.ok(token);
			
	}

}
