package com.lautaro.users_service.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.lautaro.users_service.dto.UserDTO;
import com.lautaro.users_service.model.User;

public interface UserService {

    UserDTO crear(User user);

    UserDTO obtenerPorId(@NonNull Long id);

    boolean existePorEmail(String email);

    List<UserDTO> obtenerTodos();

    UserDTO actualizar(@NonNull Long id, User user);

    void eliminar(@NonNull Long id);
}
