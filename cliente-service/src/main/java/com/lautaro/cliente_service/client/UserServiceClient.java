package com.lautaro.cliente_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lautaro.cliente_service.dto.UserDto;

/**
 * FeingClient lo que hace es decirte a Spring que genere una implmentacion de
 * runtime que hace la llamada HTTP real
 * 
 * UserServiceClientFallback es la clase que se va a usar cuando el servicio de
 * usuarios no esté disponible,
 * es decir, cuando falle la llamada HTTP. En esa clase se pueden definir
 * métodos que devuelvan respuestas predeterminadas o manejen el error de alguna
 * manera
 * específica.
 */
@FeignClient(name = "users-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto obtenerUsuarioPorId(@PathVariable Long id);

}
