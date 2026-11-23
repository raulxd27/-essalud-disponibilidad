package com.essalud.api.service;

import com.essalud.api.entity.Establecimiento;
import com.essalud.api.repository.EstablecimientoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;

    public EstablecimientoService(
            EstablecimientoRepository establecimientoRepository) {

        this.establecimientoRepository = establecimientoRepository;
    }

    // Listar establecimientos activos
    public List<Establecimiento> listarActivos() {

        return establecimientoRepository
                .findByActivoTrueOrderByNombreAsc();
    }

    // Buscar establecimiento por ID
    public Establecimiento buscarPorId(Integer id) {

        return establecimientoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Establecimiento no encontrado: " + id
                        )
                );
    }
}