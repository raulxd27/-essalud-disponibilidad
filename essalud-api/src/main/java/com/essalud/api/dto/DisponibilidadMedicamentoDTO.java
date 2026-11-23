package com.essalud.api.dto;

import java.time.LocalDateTime;

public record DisponibilidadMedicamentoDTO(
        Integer medicamentoId,
        String nombreMedicamento,
        String presentacion,
        Integer establecimientoId,
        String nombreEstablecimiento,
        Integer cantidadDisponible,
        String estadoDisponibilidad,
        LocalDateTime fechaActualizacion
) {
}