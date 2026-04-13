package com.lautaro.cliente_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lautaro.cliente_service.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id); // Verificar email único al actualizar cliente

}
