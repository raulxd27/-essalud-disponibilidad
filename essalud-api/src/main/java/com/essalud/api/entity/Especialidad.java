package com.essalud.api.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "Especialidades",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_Especialidades_Nombre",
            columnNames = "Nombre"
        )
    }
)
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EspecialidadId")
    private Integer especialidadId;

    @Column(name = "Nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "Descripcion", length = 250)
    private String descripcion;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    public Especialidad() {
    }

    public Integer getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Integer especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}