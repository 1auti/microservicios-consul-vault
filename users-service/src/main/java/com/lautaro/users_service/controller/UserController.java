package com.lautaro.users_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lautaro.users_service.dto.UserDTO;
import com.lautaro.users_service.model.User;
import com.lautaro.users_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserDTO> crear(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.crear(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> obtenerPorId(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(userService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listar() {
        return ResponseEntity.ok(userService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> actualizar(@PathVariable @NonNull Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.actualizar(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
