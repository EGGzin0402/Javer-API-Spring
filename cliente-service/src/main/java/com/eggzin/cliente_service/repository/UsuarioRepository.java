package com.eggzin.cliente_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eggzin.cliente_service.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByUsername(String username);

}
