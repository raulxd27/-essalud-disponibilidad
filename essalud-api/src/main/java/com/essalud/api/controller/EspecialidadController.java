package com.essalud.api.controller;

import com.essalud.api.dto.EspecialidadDTO;
import com.essalud.api.entity.Especialidad;
import com.essalud.api.service.EspecialidadService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(
            EspecialidadService especialidadService) {

        this.especialidadService = especialidadService;
    }

    // Consultar especialidades activas
    @GetMapping
    public List<EspecialidadDTO> listarActivas() {

        return especialidadService.listarActivos()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Convertir entidad a DTO
    private EspecialidadDTO convertirDTO(
            Especialidad especialidad) {

        return new EspecialidadDTO(
                especialidad.getEspecialidadId(),
                especialidad.getNombre(),
                especialidad.getDescripcion()
        );
    }
}
