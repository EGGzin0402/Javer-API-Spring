package com.eggzin.javer_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PasswordEditDto {
	
	@NotBlank
	private String senhaAtual;
	@NotBlank
	private String novaSenha;
	@NotBlank
	private String confirmaSenha;

}
