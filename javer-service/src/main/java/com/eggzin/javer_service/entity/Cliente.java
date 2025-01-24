package com.eggzin.javer_service.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Cliente implements Serializable {
	
	private Long id;
	private String nome;
	private Long telefone;
	private boolean correntista;
	private Float saldo;
	
    @Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}
    
    

}
