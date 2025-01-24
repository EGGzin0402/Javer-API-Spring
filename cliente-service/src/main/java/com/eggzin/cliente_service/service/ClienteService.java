package com.eggzin.cliente_service.service;

import org.springframework.stereotype.Service;

import com.eggzin.cliente_service.entity.Cliente;
import com.eggzin.cliente_service.exception.EntityNotFoundException;
import com.eggzin.cliente_service.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {
	
	private final ClienteRepository repository;
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		cliente.setSaldo(0F);
		return repository.save(cliente);						
	}
	
	@Transactional
	public Cliente buscarPorId(Long id) {
		
		return repository.findById(id).orElseThrow(
					() -> new EntityNotFoundException(String.format("Cliente com id '%s' não encontrado", id))
				);
		
	}
	
	@Transactional
	public Cliente editar(Long id, Cliente cli) {
		
		Cliente cliente = buscarPorId(id);
		
		if (cli.getNome() != null && !cli.getNome().isBlank()) cliente.setNome(cli.getNome());
		
		if (cli.getTelefone() != null) cliente.setTelefone(cli.getTelefone());
		
		return cliente;
		
	}
	
	@Transactional
	public void deletar(Long id) {
		repository.deleteById(id);
	}

}
