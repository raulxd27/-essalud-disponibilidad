package com.essalud.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record DisponibilidadCitaDTO(
        Integer disponibilidadCitaId,
        Integer especialidadId,
        String nombreEspecialidad,
        Integer establecimientoId,
        String nombreEstablecimiento,
        LocalDate fechaAtencion,
        LocalTime horaInicio,
        LocalTime horaFin,
        Integer cantidadCupos,
        String estado,
        LocalDateTime fechaActualizacion
) {
}