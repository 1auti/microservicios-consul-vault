package com.lautaro.cliente_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lautaro.cliente_service.dto.ClienteRequestDTO;
import com.lautaro.cliente_service.dto.ClienteResponseDTO;
import com.lautaro.cliente_service.mapper.ClienteMapper;
import com.lautaro.cliente_service.model.Cliente;
import com.lautaro.cliente_service.repository.ClienteRepository;
import com.lautaro.cliente_service.specification.ClienteSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AuditLogService auditoriaService;
    private final ClienteMapper clienteMapper;

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO request, Long userId) {
        if (clienteRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya existe");
        }

        if (userId == null) {
            throw new RuntimeException("User ID no puede ser null");
        }

        Cliente cliente = clienteMapper.toEntity(request, userId);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        auditoriaService.registrarCreacion(clienteGuardado, userId.toString());

        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> obtenerTodos(Pageable pageable, String nombre, String email, String apellido,
            String ciudad, Boolean activo) {
        Specification<Cliente> spec = ClienteSpecification.builder()
                .with("nombre", nombre)
                .with("apellido", apellido)
                .with("email", email)
                .with("ciudad", ciudad)
                .withExact("activo", activo)
                .build();

        return clienteRepository.findAll(spec, pageable)
                .map(clienteMapper::toResponseDTO);
    }

    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO request, Long userId) {
        Cliente cliente = buscarClientePorId(id);

        // Verificamos email único excluyendo al propio cliente
        if (clienteRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new IllegalArgumentException("Ya existe otro cliente con el email: " + request.email());
        }

        // Registramos diff campo a campo ANTES de modificar la entidad
        registrarDiff(cliente, request, userId.toString());

        // Aplicamos los cambios y guardamos
        clienteMapper.updateEntity(cliente, request);
        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id, Long userId) {
        Cliente cliente = buscarClientePorId(id);

        auditoriaService.registrarEliminacion(cliente, userId.toString());

        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .filter(Cliente::getActivo)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
    }

    /**
     * Compara el estado actual del cliente con el DTO entrante
     * y registra un log por cada campo que cambió.
     *
     * Se llama antes de modificar la entidad — así tenemos los valores anteriores.
     */
    private void registrarDiff(Cliente cliente, ClienteRequestDTO dto, String userId) {
        registrarSiCambio(cliente.getId(), "nombre", cliente.getNombre(), dto.nombre(), userId);
        registrarSiCambio(cliente.getId(), "apellido", cliente.getApellido(), dto.apellido(), userId);
        registrarSiCambio(cliente.getId(), "email", cliente.getEmail(), dto.email(), userId);
        registrarSiCambio(cliente.getId(), "telefono", cliente.getTelefono(), dto.telefono(), userId);
        registrarSiCambio(cliente.getId(), "ciudad", cliente.getCiudad(), dto.ciudad(), userId);
        registrarSiCambio(cliente.getId(), "direccion", cliente.getDireccion(), dto.direccion(), userId);
    }

    /**
     * Convierte ambos valores a String y delega en AuditLogService.
     * AuditLogService decide si registrar o ignorar según si cambiaron.
     */
    private void registrarSiCambio(Long clienteId, String campo, Object anterior, Object nuevo, String userId) {
        auditoriaService.registrarCambio(
                clienteId,
                campo,
                anterior != null ? anterior.toString() : null,
                nuevo != null ? nuevo.toString() : null,
                userId);
    }
}
