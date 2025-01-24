package com.eggzin.cliente_service.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.eggzin.cliente_service.entity.Usuario;
import com.eggzin.cliente_service.web.dto.UsuarioCreateDto;
import com.eggzin.cliente_service.web.dto.UsuarioResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioMapper {
	
	public static Usuario toUsuario(UsuarioCreateDto dto) {
		return new ModelMapper().map(dto, Usuario.class);
	}
	
	public static UsuarioResponseDto toDto(Usuario usuario) {
		return new ModelMapper().map(usuario, UsuarioResponseDto.class);
	}

}
