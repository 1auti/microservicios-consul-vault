package com.lautaro.cliente_service.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(

        @NotBlank(message = "El nombre es obligatorio") String nombre,

        @NotBlank(message = "El email es obligatorio") String email,

        @NotBlank(message = "El apellido es obligatorio") String apellido,

        String telefono,

        String direccion,

        String ciudad

) {
}
