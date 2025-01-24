package com.eggzin.cliente_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eggzin.cliente_service.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
