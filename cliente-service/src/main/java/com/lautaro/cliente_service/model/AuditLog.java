package com.lautaro.cliente_service.model;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_log")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false) // Cliente afectado por la decision es versatil esto
    private Long clienteId;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "campo_modificado")
    private String campoModificado;

    @Column(name = "tabla_modificada", nullable = false)
    private String tablaModificada;

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_nuevo")
    private String valorNuevo;

    @Column(name = "realizado_por", nullable = false)
    private String realizadoPor;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

}
