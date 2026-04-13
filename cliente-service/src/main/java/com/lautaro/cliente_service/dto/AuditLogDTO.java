package com.lautaro.cliente_service.dto;

import java.time.LocalDateTime;

public record AuditLogDTO(

        Long id,

        Long clienteId,

        String accion,

        String campoModificado,

        String tablaModificada,

        String valorAnterior,
        String valorNuevo,
        String realizadoPor,
        LocalDateTime fechaHora) {

}
