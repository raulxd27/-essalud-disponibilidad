package com.essalud.api.dto;

import java.math.BigDecimal;

public record MedicamentoDTO(
        Integer medicamentoId,
        String nombre,
        String presentacion,
        String descripcion,
        String codigoPetitorio,
        BigDecimal precioReferencial
) {
}