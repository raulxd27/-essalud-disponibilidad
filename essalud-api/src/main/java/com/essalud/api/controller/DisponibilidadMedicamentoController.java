package com.essalud.api.controller;

import com.essalud.api.dto.DisponibilidadMedicamentoDTO;
import com.essalud.api.entity.InventarioMedicamento;
import com.essalud.api.service.InventarioMedicamentoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad/medicamentos")
public class DisponibilidadMedicamentoController {

    private final InventarioMedicamentoService inventarioService;

    public DisponibilidadMedicamentoController(
            InventarioMedicamentoService inventarioService) {

        this.inventarioService = inventarioService;
    }

    // Consultar disponibilidad por establecimiento
    @GetMapping
    public List<DisponibilidadMedicamentoDTO> consultarDisponibilidad(
            @RequestParam Integer establecimientoId) {

        return inventarioService
                .listarPorEstablecimiento(establecimientoId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Convertir inventario a DTO
    private DisponibilidadMedicamentoDTO convertirDTO(
            InventarioMedicamento inventario) {

        String estado = inventario.getCantidadRegistrada() == 0
                ? "Sin disponibilidad"
                : "Disponible";

        return new DisponibilidadMedicamentoDTO(
                inventario.getMedicamento().getMedicamentoId(),
                inventario.getMedicamento().getNombre(),
                inventario.getMedicamento().getPresentacion(),
                inventario.getEstablecimiento().getEstablecimientoId(),
                inventario.getEstablecimiento().getNombre(),
                inventario.getCantidadRegistrada(),
                estado,
                inventario.getFechaActualizacion()
        );
    }
}