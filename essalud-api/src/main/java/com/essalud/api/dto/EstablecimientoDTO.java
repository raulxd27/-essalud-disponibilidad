package com.essalud.api.dto;

public record EstablecimientoDTO(
        Integer establecimientoId,
        String nombre,
        String direccion,
        String distrito,
        String provincia,
        String departamento,
        String telefono,
        Boolean activo
) {
}