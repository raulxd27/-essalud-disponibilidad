package com.essalud.api.service;

import com.essalud.api.entity.InventarioMedicamento;
import com.essalud.api.repository.InventarioMedicamentoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioMedicamentoService {

    private final InventarioMedicamentoRepository inventarioRepository;

    public InventarioMedicamentoService(
            InventarioMedicamentoRepository inventarioRepository) {

        this.inventarioRepository = inventarioRepository;
    }

    // Consultar inventario de un establecimiento
    public List<InventarioMedicamento> listarPorEstablecimiento(
            Integer establecimientoId) {

        return inventarioRepository
                .findByEstablecimiento_EstablecimientoId(
                        establecimientoId
                );
    }

    // Consultar inventario de un medicamento en un establecimiento
    public InventarioMedicamento buscarPorMedicamentoYEstablecimiento(
            Integer medicamentoId,
            Integer establecimientoId) {

        return inventarioRepository
                .findByMedicamento_MedicamentoIdAndEstablecimiento_EstablecimientoId(
                        medicamentoId,
                        establecimientoId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe inventario para el medicamento "
                                + medicamentoId
                                + " en el establecimiento "
                                + establecimientoId
                        )
                );
    }
}