package com.lautaro.cliente_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lautaro.cliente_service.dto.AuditLogDTO;
import com.lautaro.cliente_service.dto.ClienteRequestDTO;
import com.lautaro.cliente_service.dto.ClienteResponseDTO;
import com.lautaro.cliente_service.model.AuditLog;
import com.lautaro.cliente_service.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto, Long userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(@MappingTarget Cliente cliente, ClienteRequestDTO dto);

    ClienteResponseDTO toResponseDTO(Cliente cliente);

    // Genera internamente un stream().map(this::toResponseDTO).collect() — gratis
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes);

    // Todos los campos coinciden por nombre — MapStruct no necesita @Mapping
    // explícito
    AuditLogDTO toAuditLogDTO(AuditLog log);

    List<AuditLogDTO> toAuditLogDTOList(List<AuditLog> logs);
}