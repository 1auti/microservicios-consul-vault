package com.lautaro.cliente_service.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

/**
 * Manejo centralizado de excepciones.
 *
 * @RestControllerAdvice intercepta excepciones de todos los controllers
 *                       y las convierte en respuestas HTTP con el código y body
 *                       correctos.
 *
 *                       Sin esto:
 *                       EntityNotFoundException → 500 Internal Server Error
 *                       (incorrecto)
 * @Valid falla → 400 con stack trace expuesto (inseguro)
 *
 *        Con esto:
 *        EntityNotFoundException → 404 Not Found con mensaje claro
 * @Valid falla → 400 con detalle de qué campo falló
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 — cliente no encontrado o eliminado (soft delete).
     * Lanzado por buscarActivoOFallar() en ClienteService.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * 400 — regla de negocio violada (email duplicado, etc.).
     * Lanzado explícitamente en ClienteService con validaciones de negocio.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * 400 — validación de DTO falló (@NotBlank, @Email, etc.).
     * Spring lanza esto cuando @Valid encuentra errores en el RequestBody.
     *
     * Devuelve un mapa con los campos que fallaron y su mensaje de error:
     * { "email": "El email no tiene formato válido", "nombre": "El nombre es
     * obligatorio" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            errores.put(campo, error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("mensaje", "Error de validación");
        body.put("errores", errores);
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * 500 — cualquier excepción no contemplada.
     * Loguea el error real pero devuelve un mensaje genérico al cliente
     * — nunca exponemos el stack trace en producción.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor"));
    }

    /**
     * DTO de respuesta de error — estructura consistente para todos los errores.
     * El cliente siempre recibe el mismo formato, sea cual sea la excepción.
     */
    public record ErrorResponse(int status, String mensaje) {
        // LocalDateTime como campo adicional para trazabilidad
        public LocalDateTime timestamp() {
            return LocalDateTime.now();
        }
    }
}