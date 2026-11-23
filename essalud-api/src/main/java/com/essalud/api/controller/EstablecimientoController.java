package com.essalud.api.controller;

import com.essalud.api.dto.EstablecimientoDTO;
import com.essalud.api.entity.Establecimiento;
import com.essalud.api.service.EstablecimientoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/establecimientos")
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;

    public EstablecimientoController(
            EstablecimientoService establecimientoService) {
        this.establecimientoService = establecimientoService;
    }

    @GetMapping
    public List<EstablecimientoDTO> listarActivos() {

        return establecimientoService.listarActivos()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private EstablecimientoDTO convertirDTO(Establecimiento establecimiento) {

        return new EstablecimientoDTO(
                establecimiento.getEstablecimientoId(),
                establecimiento.getNombre(),
                establecimiento.getDireccion(),
                establecimiento.getDistrito(),
                establecimiento.getProvincia(),
                establecimiento.getDepartamento(),
                establecimiento.getTelefono(),
                establecimiento.getActivo()
        );
    }
}