package com.essalud.api.service;

import com.essalud.api.entity.Medicamento;
import com.essalud.api.repository.MedicamentoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(
            MedicamentoRepository medicamentoRepository) {

        this.medicamentoRepository = medicamentoRepository;
    }

    // Listar medicamentos activos
    public List<Medicamento> listarActivos() {

        return medicamentoRepository
                .findByActivoTrueOrderByNombreAsc();
    }

    // Buscar medicamentos por nombre
    public List<Medicamento> buscarPorNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return listarActivos();
        }

        return medicamentoRepository
                .findByNombreContainingIgnoreCaseAndActivoTrue(
                        nombre.trim()
                );
    }

    // Buscar medicamento por ID
    public Medicamento buscarPorId(Integer id) {

        return medicamentoRepository.findById(id)
                .filter(Medicamento::getActivo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicamento no encontrado o inactivo: " + id
                        )
                );
    }
}