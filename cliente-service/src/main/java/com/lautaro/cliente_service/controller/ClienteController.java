package com.lautaro.cliente_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lautaro.cliente_service.dto.ClienteRequestDTO;
import com.lautaro.cliente_service.dto.ClienteResponseDTO;
import com.lautaro.cliente_service.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST del cliente-service.
 *
 * Responsabilidades del Controller — y solo estas:
 * - Recibir la request HTTP
 * - Extraer el userId del header X-User-Id
 * - Delegar en ClienteService
 * - Devolver el ResponseEntity con el código HTTP correcto
 *
 * NO tiene lógica de negocio — eso vive en ClienteService.
 * NO accede al repositorio directamente.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private static final String HEADER_USER_ID = "X-User-Id";

    private final ClienteService clienteService;

    /**
     * POST /clientes
     * Crea un nuevo cliente.
     *
     * @Valid dispara la validación de ClienteRequestDTO antes de entrar al método.
     *        Si alguna validación falla (@NotBlank, @Email) Spring devuelve 400
     *        automáticamente.
     *
     *        El userId viene del header X-User-Id que propaga el Gateway — no del
     *        body.
     *        201 Created es el código correcto para una creación exitosa.
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(
            @Valid @RequestBody ClienteRequestDTO dto,
            @RequestHeader(HEADER_USER_ID) String userId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                // Conversión String → Long acá, no en el service
                // El controller es quien recibe datos HTTP crudos y los convierte
                .body(clienteService.crear(dto, Long.parseLong(userId)));
    }

    /**
     * GET /clientes/{id}
     * Obtiene un cliente por ID.
     *
     * 200 OK si existe y está activo.
     * 404 Not Found si no existe o fue eliminado (soft delete) — lo maneja
     * GlobalExceptionHandler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    /**
     * GET /clientes
     * Lista clientes con filtros opcionales y paginación.
     *
     * Parámetros de query opcionales:
     * ?nombre=lau&ciudad=buenos+aires&activo=true&page=0&size=10&sort=nombre,asc
     *
     * @PageableDefault: si no mandan page/size, usamos page=0, size=20 por defecto.
     *                   Devuelve Page<ClienteResponseDTO> que incluye
     *                   totalElements, totalPages, etc.
     */
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(
                // Nombre del método alineado con tu service: obtenerTodos
                clienteService.obtenerTodos(pageable, nombre, email, apellido, ciudad, activo));
    }

    /**
     * PUT /clientes/{id}
     * Actualiza un cliente existente.
     *
     * Usamos PUT (reemplazo completo) y no PATCH (parcial) porque
     * ClienteRequestDTO tiene todos los campos del cliente.
     * Si quisieras actualización parcial, usarías PATCH con un DTO diferente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto,
            @RequestHeader(HEADER_USER_ID) String userId) {
        return ResponseEntity.ok(clienteService.actualizar(id, dto, Long.parseLong(userId)));
    }

    /**
     * DELETE /clientes/{id}
     * Soft delete — marca el cliente como inactivo.
     *
     * 204 No Content: la operación fue exitosa pero no hay body que devolver.
     * Es el código correcto para un DELETE exitoso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader(HEADER_USER_ID) String userId) {
        clienteService.eliminar(id, Long.parseLong(userId));
        return ResponseEntity.noContent().build();
    }
}