package com.eggzin.cliente_service.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.eggzin.cliente_service.entity.Cliente;
import com.eggzin.cliente_service.web.dto.ClienteCreateDto;
import com.eggzin.cliente_service.web.dto.ClienteEditDto;
import com.eggzin.cliente_service.web.dto.ClienteResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {
	
	public static Cliente toCliente(ClienteCreateDto dto) {
		return new ModelMapper().map(dto, Cliente.class);
	}
	
	public static Cliente toCliente(ClienteEditDto dto) {
		return new ModelMapper().map(dto, Cliente.class);
	}
	
	public static ClienteResponseDto toDto(Cliente cliente) {
		return new ModelMapper().map(cliente,ClienteResponseDto.class);
	}

}
