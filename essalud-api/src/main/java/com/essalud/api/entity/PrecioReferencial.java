package com.essalud.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "PreciosReferenciales",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_Precios_Medicamento_Fecha_Fuente",
            columnNames = {
                "MedicamentoId",
                "FechaConsulta",
                "Fuente"
            }
        )
    }
)
public class PrecioReferencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PrecioId")
    private Integer precioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MedicamentoId", nullable = false)
    private Medicamento medicamento;

    @Column(name = "PrecioReferencial", nullable = false, precision = 12, scale = 4)
    private BigDecimal precioReferencial;

    @Column(name = "FechaConsulta", nullable = false)
    private LocalDate fechaConsulta;

    @Column(name = "Fuente", nullable = false, length = 150)
    private String fuente;

    @Column(name = "Observacion", length = 300)
    private String observacion;

    public PrecioReferencial() {
    }

    public Integer getPrecioId() {
        return precioId;
    }

    public void setPrecioId(Integer precioId) {
        this.precioId = precioId;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public BigDecimal getPrecioReferencial() {
        return precioReferencial;
    }

    public void setPrecioReferencial(BigDecimal precioReferencial) {
        this.precioReferencial = precioReferencial;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}