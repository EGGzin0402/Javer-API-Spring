package com.eggzin.javer_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClienteCreateDto {
	
	@NotBlank
	private String nome;
	@Size(min = 11, max = 11)
	private String telefone;
	private boolean correntista;

}
