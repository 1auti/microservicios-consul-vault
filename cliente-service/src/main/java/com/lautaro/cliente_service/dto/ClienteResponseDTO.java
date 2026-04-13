package com.lautaro.cliente_service.dto;

import java.time.LocalDateTime;

public record ClienteResponseDTO(

        Long id,

        String nombre,

        String email,

        String apellido,

        String telefono,

        String direccion,

        String ciudad,

        Long userId,
        Boolean activo,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy) {

}
