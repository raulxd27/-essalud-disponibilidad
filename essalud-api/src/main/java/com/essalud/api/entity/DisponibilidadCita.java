package com.essalud.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "DisponibilidadCitas",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_Citas_Horario",
            columnNames = {
                "EspecialidadId",
                "EstablecimientoId",
                "FechaAtencion",
                "HoraInicio",
                "HoraFin"
            }
        )
    }
)
public class DisponibilidadCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DisponibilidadCitaId")
    private Integer disponibilidadCitaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EspecialidadId", nullable = false)
    private Especialidad especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EstablecimientoId", nullable = false)
    private Establecimiento establecimiento;

    @Column(name = "FechaAtencion", nullable = false)
    private LocalDate fechaAtencion;

    @Column(name = "HoraInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "HoraFin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "CantidadCupos", nullable = false)
    private Integer cantidadCupos;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "FechaActualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ActualizadoPor", nullable = false)
    private Usuario actualizadoPor;

    public DisponibilidadCita() {
    }

    public Integer getDisponibilidadCitaId() {
        return disponibilidadCitaId;
    }

    public void setDisponibilidadCitaId(Integer disponibilidadCitaId) {
        this.disponibilidadCitaId = disponibilidadCitaId;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getCantidadCupos() {
        return cantidadCupos;
    }

    public void setCantidadCupos(Integer cantidadCupos) {
        this.cantidadCupos = cantidadCupos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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