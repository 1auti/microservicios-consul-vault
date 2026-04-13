package com.lautaro.cliente_service.client;

import org.springframework.stereotype.Component;

import com.lautaro.cliente_service.dto.UserDto;

import jakarta.ws.rs.ServiceUnavailableException;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserDto obtenerUsuarioPorId(Long id) {
        throw new ServiceUnavailableException(
                "Servicio de usuarios no disponible. No se pudo obtener información del usuario: " + id);
    }
}
