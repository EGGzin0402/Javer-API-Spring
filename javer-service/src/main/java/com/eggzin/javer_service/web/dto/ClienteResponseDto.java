package com.eggzin.javer_service.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClienteResponseDto {
	
	private String nome;
	private Long telefone;
	private boolean correntista;
	private Float saldo;
	
	private String port;

}