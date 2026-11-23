package com.essalud.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActualizarDisponibilidadCitaDTO(
        Integer especialidadId,
        Integer establecimientoId,
        LocalDate fechaAtencion,
        LocalTime horaInicio,
        LocalTime horaFin,
        Integer cantidadCupos
) {
}