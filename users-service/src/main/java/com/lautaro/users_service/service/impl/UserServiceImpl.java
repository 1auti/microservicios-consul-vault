package com.lautaro.users_service.service.impl;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.lautaro.users_service.dto.UserDTO;
import com.lautaro.users_service.model.User;
import com.lautaro.users_service.repository.UserRepository;
import com.lautaro.users_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO crear(User user) {
        // Validaciones null
        if (user.getNombre() == null || user.getNombre().isBlank())
            throw new RuntimeException("El nombre es requerido");
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new RuntimeException("El email es requerido");
        if (user.getPassword() == null || user.getPassword().isBlank())
            throw new RuntimeException("La password es requerida");

        // Verificamos email duplicado
        if (userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Ya existe un usuario con el email: " + user.getEmail());

        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO obtenerPorId(@NonNull Long id) {
        // Una sola query — orElseThrow maneja el caso not found
        return toDTO(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id)));
    }

    @Override
    public UserDTO actualizar(@NonNull Long id, User userActualizado) {
        // Validaciones null
        if (userActualizado.getNombre() == null || userActualizado.getNombre().isBlank())
            throw new RuntimeException("El nombre es requerido");
        if (userActualizado.getEmail() == null || userActualizado.getEmail().isBlank())
            throw new RuntimeException("El email es requerido");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        user.setNombre(userActualizado.getNombre());
        user.setEmail(userActualizado.getEmail());

        // Solo actualizamos password si viene en el request
        if (userActualizado.getPassword() != null && !userActualizado.getPassword().isBlank())
            user.setPassword(userActualizado.getPassword());

        // save() persiste los cambios en la BD
        return toDTO(userRepository.save(user));
    }

    @Override
    public void eliminar(@NonNull Long id) {
        if (!userRepository.existsById(id))
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        userRepository.deleteById(id);
    }

    // password nunca sale del servicio
    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getNombre(), user.getEmail(), user.getPassword());
    }

    @Override
    public boolean existePorEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existePorEmail'");
    }

    @Override
    public List<UserDTO> obtenerTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }
}