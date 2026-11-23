package com.essalud.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MedicamentoId")
    private Integer medicamentoId;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "Presentacion", nullable = false, length = 150)
    private String presentacion;

    @Column(name = "Descripcion", length = 300)
    private String descripcion;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    @Column(name = "FechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "CodigoPetitorio", length = 30)
    private String codigoPetitorio;

    @Column(name = "PrecioReferencial", precision = 10, scale = 2)
    private BigDecimal precioReferencial;

    public Medicamento() {
    }

    public Integer getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Integer medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoPetitorio() {
        return codigoPetitorio;
    }

    public void setCodigoPetitorio(String codigoPetitorio) {
        this.codigoPetitorio = codigoPetitorio;
    }

    public BigDecimal getPrecioReferencial() {
        return precioReferencial;
    }

    public void setPrecioReferencial(BigDecimal precioReferencial) {
        this.precioReferencial = precioReferencial;
    }
}