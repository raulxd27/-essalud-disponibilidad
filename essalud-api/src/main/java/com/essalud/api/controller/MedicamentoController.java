package com.essalud.api.controller;

import com.essalud.api.dto.MedicamentoDTO;
import com.essalud.api.entity.Medicamento;
import com.essalud.api.service.MedicamentoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(
            MedicamentoService medicamentoService) {

        this.medicamentoService = medicamentoService;
    }

    // Listar medicamentos activos
    @GetMapping
    public List<MedicamentoDTO> listarMedicamentos(
            @RequestParam(required = false) String nombre) {

        List<Medicamento> medicamentos;

        if (nombre != null && !nombre.isBlank()) {
            medicamentos = medicamentoService.buscarPorNombre(nombre);
        } else {
            medicamentos = medicamentoService.listarActivos();
        }

        return medicamentos.stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Convertir entidad a DTO
    private MedicamentoDTO convertirDTO(Medicamento medicamento) {

        return new MedicamentoDTO(
                medicamento.getMedicamentoId(),
                medicamento.getNombre(),
                medicamento.getPresentacion(),
                medicamento.getDescripcion(),
                medicamento.getCodigoPetitorio(),
                medicamento.getPrecioReferencial()
        );
    }
}