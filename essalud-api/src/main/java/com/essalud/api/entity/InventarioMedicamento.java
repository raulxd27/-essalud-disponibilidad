package com.essalud.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "InventarioMedicamentos",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_Inventario_Medicamento_Establecimiento",
            columnNames = {"MedicamentoId", "EstablecimientoId"}
        )
    }
)
public class InventarioMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InventarioId")
    private Integer inventarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MedicamentoId", nullable = false)
    private Medicamento medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EstablecimientoId", nullable = false)
    private Establecimiento establecimiento;

    @Column(name = "CantidadRegistrada", nullable = false)
    private Integer cantidadRegistrada;

    @Column(name = "FechaActualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ActualizadoPor", nullable = false)
    private Usuario actualizadoPor;

    public InventarioMedicamento() {
    }

    public Integer getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Integer inventarioId) {
        this.inventarioId = inventarioId;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Integer getCantidadRegistrada() {
        return cantidadRegistrada;
    }

    public void setCantidadRegistrada(Integer cantidadRegistrada) {
        this.cantidadRegistrada = cantidadRegistrada;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Usuario getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(Usuario actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }
}