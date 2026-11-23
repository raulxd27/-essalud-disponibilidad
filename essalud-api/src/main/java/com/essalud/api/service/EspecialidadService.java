package com.essalud.api.service;

import com.essalud.api.entity.Especialidad;
import com.essalud.api.repository.EspecialidadRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(
            EspecialidadRepository especialidadRepository) {

        this.especialidadRepository = especialidadRepository;
    }

    // Listar especialidades activas
    public List<Especialidad> listarActivos() {

        return especialidadRepository
                .findByActivoTrueOrderByNombreAsc();
    }

    // Buscar especialidad por ID
    public Especialidad buscarPorId(Integer id) {

        return especialidadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Especialidad no encontrada: " + id
                        )
                );
    }
}
