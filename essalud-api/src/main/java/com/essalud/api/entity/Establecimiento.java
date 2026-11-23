package com.essalud.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Establecimientos")
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EstablecimientoId")
    private Integer establecimientoId;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "Direccion", nullable = false, length = 250)
    private String direccion;

    @Column(name = "Distrito", length = 100)
    private String distrito;

    @Column(name = "Provincia", length = 100)
    private String provincia;

    @Column(name = "Departamento", length = 100)
    private String departamento;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    @Column(name = "FechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Establecimiento() {
    }

    public Integer getEstablecimientoId() {
        return establecimientoId;
    }

    public void setEstablecimientoId(Integer establecimientoId) {
        this.establecimientoId = establecimientoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
}