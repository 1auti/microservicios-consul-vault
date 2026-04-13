package com.lautaro.cliente_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.lautaro.cliente_service.constantes.ClienteConstants;
import com.lautaro.cliente_service.dto.AuditLogDTO;
import com.lautaro.cliente_service.mapper.ClienteMapper;
import com.lautaro.cliente_service.model.AuditLog;
import com.lautaro.cliente_service.model.Cliente;
import com.lautaro.cliente_service.repository.AuditLogRepository;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ClienteMapper mapper;

    public void registrarCreacion(Cliente cliente, String realizadoPor) {
        AuditLog log = AuditLog.builder()
                .clienteId(cliente.getId())
                .accion(ClienteConstants.ACCION_CREAR)
                .tablaModificada(ClienteConstants.TABLE_NAME)
                .valorNuevo("id=" + cliente.getId())
                .realizadoPor(realizadoPor)
                .fechaHora(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public void registrarCambio(@NonNull Long clienteId, String campo,
            String valorAnterior, String valorNuevo,
            @NonNull String realizadoPor) {
        if (Objects.equals(valorAnterior, valorNuevo))
            return;

        AuditLog log = AuditLog.builder()
                .clienteId(clienteId)
                .accion(ClienteConstants.ACCION_ACTUALIZAR)
                .tablaModificada(ClienteConstants.TABLE_NAME)
                .campoModificado(campo)
                .valorAnterior(valorAnterior)
                .valorNuevo(valorNuevo)
                .realizadoPor(realizadoPor)
                .fechaHora(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public void registrarEliminacion(Cliente cliente, String realizadoPor) {
        AuditLog log = AuditLog.builder()
                .clienteId(cliente.getId())
                .accion(ClienteConstants.ACCION_ELIMINAR)
                .tablaModificada(ClienteConstants.TABLE_NAME)
                .campoModificado("activo")
                .valorAnterior("true")
                .valorNuevo("false")
                .realizadoPor(realizadoPor)
                .fechaHora(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public List<AuditLogDTO> obtenerHistorial(Long clienteId) {
        return mapper.toAuditLogDTOList(
                auditLogRepository.findByClienteIdOrderByFechaHoraDesc(clienteId));
    }

}
