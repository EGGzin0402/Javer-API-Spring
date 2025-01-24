package com.eggzin.cliente_service.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggzin.cliente_service.entity.Usuario;
import com.eggzin.cliente_service.exception.EntityNotFoundException;
import com.eggzin.cliente_service.exception.PasswordInvalidException;
import com.eggzin.cliente_service.exception.UsernameUniqueViolationException;
import com.eggzin.cliente_service.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		try {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException ex) {
			throw new UsernameUniqueViolationException(String.format("Usuário '%s' já cadastrado", usuario.getUsername()));
		}
	}
	
	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException(String.format("Usuário com id '%s' não encontrado", id))
			);
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorUsername(String username) {
		return usuarioRepository.findByUsername(username).orElseThrow(
				() -> new EntityNotFoundException(String.format("Usuário com username '%s' não encontrado", username))
			);
	}
	
	@Transactional
	public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
		if(!novaSenha.equals(confirmaSenha)) {
			throw new PasswordInvalidException("Nova senha e confirmação de senha não conferem.");
		}
		
		Usuario usuario = buscarPorId(id);
		
		if(!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
			throw new PasswordInvalidException("Sua senha atual não confere.");
		}
		
		usuario.setPassword(passwordEncoder.encode(novaSenha));
		return usuario;
	}
	

}
