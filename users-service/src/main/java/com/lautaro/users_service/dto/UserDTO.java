package com.lautaro.users_service.dto;

public record UserDTO(
                Long id,
                String nombre,
                String password,
                String email) {

}
