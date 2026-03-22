package com._auti.config_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer // Le dice a Spring Boot que esta aplicación es un servidor de configuración
@EnableDiscoveryClient // Permite que esta aplicación se registre en el servicio de descubrimiento
						// (Eureka, Cosul, etc.)
public class ConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServiceApplication.class, args);
	}

}
