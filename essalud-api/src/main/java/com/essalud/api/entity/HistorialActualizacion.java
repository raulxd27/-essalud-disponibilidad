package com.essalud.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HistorialActualizaciones")
public class HistorialActualizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistorialId")
    private Long historialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioId", nullable = false)
    private Usuario usuario;

    @Column(name = "TipoRegistro", nullable = false, length = 50)
    private String tipoRegistro;

    @Column(name = "RegistroId", nullable = false)
    private Integer registroId;

    @Column(name = "Accion", nullable = false, length = 30)
    private String accion;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @Column(name = "ValorAnterior", columnDefinition = "nvarchar(max)")
    private String valorAnterior;

    @Column(name = "ValorNuevo", columnDefinition = "nvarchar(max)")
    private String valorNuevo;

    @Column(name = "FechaActualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public HistorialActualizacion() {
    }

    public Long getHistorialId() {
        return historialId;
    }

    public void setHistorialId(Long historialId) {
        this.historialId = historialId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Integer getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Integer registroId) {
        this.registroId = registroId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}