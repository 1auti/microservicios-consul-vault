package com.lautaro.cliente_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lautaro.cliente_service.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByClienteIdOrderByFechaHoraDesc(Long clienteId);

}
