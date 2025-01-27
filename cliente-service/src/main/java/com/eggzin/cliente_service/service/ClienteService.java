package com.eggzin.cliente_service.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eggzin.cliente_service.entity.Cliente;
import com.eggzin.cliente_service.exception.EntityNotFoundException;
import com.eggzin.cliente_service.exception.UsernameUniqueViolationException;
import com.eggzin.cliente_service.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {
	
	private final ClienteRepository repository;
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		try {
			cliente.setSaldo(0F);
			return repository.save(cliente);									
		} catch (DataIntegrityViolationException ex) {
			throw new UsernameUniqueViolationException(String.format("O Usuário '%s' já está cadastrado como cliente", cliente.getUsuario().getUsername()));
		}
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
	
	@Transactional
	public Cliente depositar(String deposito, Long id) {
		Cliente cliente = buscarPorId(id);
		cliente.setSaldo(cliente.getSaldo()+Float.parseFloat(deposito));
		return cliente;
	}
	
	@Transactional
	public Cliente sacar(String saque, Long id) {
		Cliente cliente = buscarPorId(id);
		cliente.setSaldo(cliente.getSaldo()-Float.parseFloat(saque));
		return cliente;
	}

}
