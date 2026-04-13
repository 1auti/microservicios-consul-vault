package com.lautaro.cliente_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lautaro.cliente_service.dto.AuditLogDTO;
import com.lautaro.cliente_service.service.AuditLogService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador del historial de auditoría.
 *
 * Separado de ClienteController porque es una responsabilidad distinta —
 * consultar el historial no es una operación de negocio sobre clientes,
 * es una operación de trazabilidad/auditoría.
 *
 * En un sistema bancario real este endpoint estaría protegido con un rol
 * específico (AUDITOR, ADMIN) — lo dejamos preparado con el comentario.
 */
@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * GET /audit/clientes/{id}
     * Devuelve el historial completo de cambios de un cliente,
     * ordenado del más reciente al más antiguo.
     *
     * Ejemplo de uso:
     * GET /audit/clientes/5
     * → [{accion: "ACTUALIZAR", campo: "email", anterior: "a@x.com", nuevo:
     * "b@x.com", ...}]
     *
     * Si el cliente no tuvo cambios, devuelve lista vacía con 200 OK —
     * no tiene sentido devolver 404 porque la ausencia de logs es válida.
     */
    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<List<AuditLogDTO>> obtenerHistorial(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(auditLogService.obtenerHistorial(clienteId));
    }
}